package com.supshop.suppingmall.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.delivery.DeliveryService;
import com.supshop.suppingmall.mapper.OrderItemMapper;
import com.supshop.suppingmall.mapper.OrderMapper;
import com.supshop.suppingmall.payment.Payment;
import com.supshop.suppingmall.payment.PaymentService;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductOption;
import com.supshop.suppingmall.product.ProductService;
import com.supshop.suppingmall.user.UserService;
import com.supshop.suppingmall.user.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final ProductService productService;
    private final UserService userService;
    private final OrderItemMapper orderItemMapper;
    private final PaymentService paymentService;
    private final DeliveryService deliveryService;

    public Orders findOrder(Long orderId) {
        Optional<Orders> order = orderMapper.findOne(orderId);
        return order.orElseThrow(NoSuchElementException::new);
    }

    @Transactional
    public Long order(OrderForm orderForm) {

        //실제 주문 상태로 변경
        Orders order = orderMapper.findOne(orderForm.getOrderId()).get();
        order.setStatus(Orders.OrderStatus.ORDER);

        //결제 내용 추가
        Payment payment = orderForm.getPayment();
        Long paymentId = paymentService.save(payment);

        //배송 내용 추가
        Delivery delivery = orderForm.getDelivery();
        Long deliveryId = deliveryService.save(delivery);

        //상품 수량 변경
        List<OrderItem> orderItemList = order.getOrderItems();
        List<ProductOption> productOptionList = new ArrayList<>();
        for(OrderItem orderItem : orderItemList) {
            ProductOption productOption = orderItem.getProductOption();
            productOption.removeStock(orderItem.getCount());
            productOptionList.add(productOption);
        }

        productService.updateProductOption(productOptionList);

        //주문상태 변경, 결제, 배송 내용 수정
        orderMapper.order(order.getOrderId(),order.getStatus(),deliveryId,paymentId);

        return order.getOrderId();
    }

    @Transactional
    public Orders createOrder(TempOrderForm tempOrderForm) {

        //임시 주문 생성
        Orders orders = this.setTempOrder(tempOrderForm);
        orderMapper.save(orders);

        //주문상품 생성
        for(OrderItem orderItem : orders.getOrderItems()) {
            orderItem.setOrders(orders);
        }
        orderItemMapper.saveList(orders.getOrderItems());
        return orders;
    }



    private Orders setTempOrder(TempOrderForm tempOrderForm) {

        // 임시 주문 생성을 위한 정보 가져오기
        List<OrderItem> orderItems = this.setOrderItemsInfo(tempOrderForm);
        UserVO buyer = userService.getUserVO(tempOrderForm.getBuyerId());
        UserVO seller = userService.getUserVO(tempOrderForm.getSellerId());

        //임시 주문 생성 (주문상품, 구매자, 판매자)
        Orders tempOrder = Orders.createTempOrder(orderItems,buyer,seller);

        return tempOrder;
    }

    /**
     * set Product, ProductOption Information
     * @param tempOrderForm
     * @return List<OrderItem>
     */
    private List<OrderItem> setOrderItemsInfo(TempOrderForm tempOrderForm){

        // Json To OrderItem
        List<OrderItem> orderItems = this.setJsonToOrderItem(tempOrderForm.getOrderItems());

        // 상품조회
        Product product = productService.retrieveProduct(orderItems.get(0).getProduct().getProductId());


        for (OrderItem orderItem : orderItems) {
            int optionId = orderItem.getProductOption().getOptionId();
            orderItem.setProductOption(product.getOptions().get(optionId - 1));
            orderItem.setProduct(product);
        }

        return orderItems;
    }

    /**
     * Json To OrderItem List
     * @param orderItems
     * @return List<OrderItem>
     */
    private List<OrderItem> setJsonToOrderItem(String orderItems) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderItem> items = null;

        try {
            items = Arrays.asList(objectMapper.readValue(orderItems, OrderItem[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }




}
