package com.supshop.suppingmall.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.delivery.DeliveryService;
import com.supshop.suppingmall.mapper.OrderMapper;
import com.supshop.suppingmall.order.form.TempOrderForm;
import com.supshop.suppingmall.page.TenItemsCriteria;
import com.supshop.suppingmall.payModule.ModuleController;
import com.supshop.suppingmall.payment.Payment;
import com.supshop.suppingmall.payment.PaymentService;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductOption;
import com.supshop.suppingmall.product.ProductService;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final PaymentService paymentService;
    private final DeliveryService deliveryService;
    private final OrderItemService orderItemService;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ModuleController moduleController;

    private static final String payModuleUrl = "/payModule";
    private static final int hour = 23;
    private static final int minute = 59;

    public int findCount(String type, Long id) {
        return orderMapper.findCount(type, id);
    }

    public List<Orders> getOrderList(LocalDate fromDate, LocalDate toDate, Orders.OrderStatus orderStatus, TenItemsCriteria criteria) {
        LocalDateTime formDateTime = Optional.ofNullable(fromDate).map(LocalDate::atStartOfDay).orElse(null);
        LocalDateTime toDateTime = Optional.ofNullable(toDate).map(localDate -> toDate.atTime(hour, minute)).orElse(null);

        //code : orderStatus Value
        String code = Optional.ofNullable(orderStatus).map(Orders.OrderStatus::getCode).orElse(null);
        List<Orders> ordersList = orderMapper.findAll(formDateTime, toDateTime,code,criteria);
        return ordersList;
    }

    public Orders getOrder(Long orderId) {
        Optional<Orders> order = orderMapper.findOne(orderId);
        return order.orElseThrow(NoSuchElementException::new);
    }

    //구매자의 관점에서 주문을 조회
    public List<Orders> getOrderByBuyerId(Long userId, LocalDate fromDate, LocalDate toDate, String type, Orders.OrderStatus status) {
        LocalDateTime formDateTime = Optional.ofNullable(fromDate).map(LocalDate::atStartOfDay).orElse(null);
        LocalDateTime toDateTime = Optional.ofNullable(toDate).map(localDate -> toDate.atTime(hour, minute)).orElse(null);
        //code : orderStatus Value
        String code = Optional.ofNullable(status).map(Orders.OrderStatus::getCode).orElse(null);
        List<Orders> ordersList = orderMapper.findByBuyerId(userId, formDateTime, toDateTime, type,  code);
        return ordersList;
    }

    //판매자의 관점에서 주문을 조회
    public List<Orders> getOrderBySellerId(Long userId, LocalDate fromDate, LocalDate toDate, String type, Delivery.DeliveryStatus deliveryStatus, Orders.OrderStatus orderStatus, TenItemsCriteria criteria) {
        LocalDateTime formDateTime = Optional.ofNullable(fromDate).map(LocalDate::atStartOfDay).orElse(null);
        LocalDateTime toDateTime = Optional.ofNullable(toDate).map(localDate -> toDate.atTime(hour, minute)).orElse(null);

        //code : orderStatus Value
        String code = "";
        if(type != null && "delivery".equals(type)) {
            code = Optional.ofNullable(deliveryStatus).map(Delivery.DeliveryStatus::getCode).orElse(null);
        } else if(type != null && "order".equals(type)) {
            code = Optional.ofNullable(orderStatus).map(Orders.OrderStatus::getCode).orElse(null);
        }
        List<Orders> ordersList = orderMapper.findBySellerId(userId, formDateTime, toDateTime, type,  code, criteria);
        return ordersList;
    }

    /**
     * 임시 주문 상태로 DB에 데이터 생성
     * @param tempOrderFormList
     * @return orders
     */
    @Transactional
    public Orders createTempOrder(List<TempOrderForm> tempOrderFormList) {

        //임시 주문 생성
        Orders orders = setTempOrder(tempOrderFormList);
        orderMapper.save(orders);

        //주문상품에 생성된 주문 정보를 첨부
        for(OrderItem orderItem : orders.getOrderItems()) {
            orderItem.setOrders(orders);
        }
        orderItemService.saveList(orders.getOrderItems());
        return orders;
    }

    /**
     * 임시 주문 생성을 위한 정보 셋팅
     * @param tempOrderFormList
     * @return Order
     */
    private Orders setTempOrder(List<TempOrderForm> tempOrderFormList) {
        List<OrderItem> orderItemList = new ArrayList<>();
        for(TempOrderForm form : tempOrderFormList) {
            // 1. 상품조회
            Product product = productService.getProduct(form.getProductId());

            // 2. 주문자 및 구매자 정보 조회
            User buyer = userService.getUser(form.getBuyerId());
            User seller = userService.getUser(form.getSellerId());

            // 3. 주문상품 목록 생성
            setOrderItemsInfo(form,product,buyer,seller,orderItemList);
        }

        //임시 주문 생성
        Orders tempOrder = Orders.builder()
                                    .orderItems(orderItemList)
                                    .status(Orders.OrderStatus.WAIT)
                                    .build();

        return tempOrder;
    }

    /**
     * 가져온 상품정보를 이용해 상품 옵션의 필요내용 설정
     * @param tempOrderForm
     * @return List<OrderItem>
     */
    private void setOrderItemsInfo(TempOrderForm tempOrderForm, Product product,User buyer,User seller, List<OrderItem> orderItemList){

        List<OrderItem> orderItems = tempOrderForm.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            // 추가정보 입력
            int optionId = orderItem.getProductOption().getOptionId();
            orderItem.setProductOption(product.getOptions().get(optionId - 1));
            orderItem.setPrice(product.getOptions().get(optionId - 1).getPrice());
            orderItem.setProduct(product);
            orderItem.setStatus(Orders.OrderStatus.WAIT);
            orderItem.setBuyer(buyer);
            orderItem.setSeller(seller);

            //하나의 orderItemList에 담아서 order에 넣어주기 위함
            orderItemList.add(orderItem);
        }
    }

    /**
     * 실제 주문 : 주문, 결제, 배송 정보 생성
     * @param orders
     * @param payment
     * @param delivery
     * @return
     */
    @Transactional
    public Orders order(Orders orders,Payment payment, Delivery delivery) {
        //임시 주문상태를 주문 상태로 변경
        orders.setStatus(Orders.OrderStatus.COMPLETE);

        List<OrderItem> orderItems = orders.getOrderItems();
        List<ProductOption> productOptionList = new ArrayList<>();
        List<Payment> paymentList = new ArrayList<>();
        List<Delivery> deliveryList = new ArrayList<>();

        for(OrderItem orderItem : orderItems) {
            //임시 주문상태를 실제 주문 상태로 변경
            orderItem.setOrders(orders);
            orderItem.setStatus(Orders.OrderStatus.ORDER);

            //상품 수량 감소
            ProductOption productOption = orderItem.getProductOption();
            productOption.setProductId(orderItem.getProduct().getProductId());
            productOption.removeStock(orderItem.getCount());
            productOptionList.add(productOption);

            // 결제 정보 생성
            Payment mappedPayment = new Payment();
            mappedPayment.setPaymentType(payment.getPaymentType());
            mappedPayment.setVendorCheckNumber(payment.getVendorCheckNumber());
            mappedPayment.setStatus(payment.getStatus());
            mappedPayment.setOrderItem(orderItem);
            mappedPayment.setPrice(orderItem.getPrice());
            paymentList.add(mappedPayment);

            // 배송 정보 생성
            Delivery mappedDelivery = new Delivery();
            mappedDelivery.setOrderItem(orderItem);
            mappedDelivery.setName(delivery.getName());
            mappedDelivery.setPhone(delivery.getPhone());
            mappedDelivery.setZipCode(delivery.getZipCode());
            mappedDelivery.setAddress(delivery.getAddress());
            mappedDelivery.setAddressDetail(delivery.getAddressDetail());
            mappedDelivery.setVendor(delivery.getVendor());
            mappedDelivery.setMemo(delivery.getMemo());
            deliveryList.add(mappedDelivery);
        }


        //주문상태 변경, 물품갯수 감소, 결제 및 배송정보 생성
        orderMapper.updateOrder(orders);
        orderItemService.updateOrderItemList(orderItems);
        productService.updateProductOption(productOptionList);
        paymentService.save(paymentList);
        deliveryService.save(deliveryList);
        orders.setOrderItems(orderItems);


        return orders;
    }


}
