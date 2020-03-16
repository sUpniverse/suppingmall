package com.supshop.suppingmall.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supshop.suppingmall.mapper.OrderItemMapper;
import com.supshop.suppingmall.mapper.OrderMapper;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductService;
import com.supshop.suppingmall.user.UserService;
import com.supshop.suppingmall.user.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final ProductService productService;
    private final UserService userService;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    public Long order(Orders orders) {
        //실제 주문 상태로 변경
        orderMapper.save(orders);
        //상품 수량 변경

        return orders.getOrderId();
    }

    @Transactional
    public Orders createTempOrder(TempOrderForm tempOrderForm) {

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

    public Orders retrieveOrder(Long orderId) {
        Optional<Orders> order = orderMapper.findOne(orderId);
        return order.orElseThrow(NoSuchElementException::new);
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
