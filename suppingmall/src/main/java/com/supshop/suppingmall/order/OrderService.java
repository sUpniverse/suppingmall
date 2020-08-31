package com.supshop.suppingmall.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.delivery.DeliveryService;
import com.supshop.suppingmall.mapper.OrderItemMapper;
import com.supshop.suppingmall.mapper.OrderMapper;
import com.supshop.suppingmall.order.Form.OrderForm;
import com.supshop.suppingmall.order.Form.TempOrderForm;
import com.supshop.suppingmall.page.OrderCriteria;
import com.supshop.suppingmall.payModule.ModuleController;
import com.supshop.suppingmall.payment.Payment;
import com.supshop.suppingmall.payment.PaymentService;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductOption;
import com.supshop.suppingmall.product.ProductService;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ModuleController moduleController;

    private static final String payModuleUrl = "/payModule";
    private static final int hour = 23;
    private static final int minute = 59;

    public Orders findOrder(Long orderId) {
        Optional<Orders> order = orderMapper.findOne(orderId);
        return order.orElseThrow(NoSuchElementException::new);
    }

    //구매자의 관점에서 주문을 조회
    public List<Orders> findOrderByBuyerId(Long userId, LocalDate fromDate, LocalDate toDate, String type, Orders.OrderStatus status) {
        LocalDateTime formDateTime = Optional.ofNullable(fromDate).map(LocalDate::atStartOfDay).orElse(null);
        LocalDateTime toDateTime = Optional.ofNullable(toDate).map(localDate -> toDate.atTime(hour, minute)).orElse(null);
        String code = Optional.ofNullable(status).map(Orders.OrderStatus::getCode).orElse(null);
        List<Orders> ordersList = orderMapper.findByBuyerId(userId, formDateTime, toDateTime, type,  code);
        return ordersList;
    }

    //판매자의 관점에서 주문을 조회
    public List<Orders> findOrderBySellerId(Long userId, LocalDate fromDate, LocalDate toDate, String type, Delivery.DeliveryStatus deliveryStatus, Orders.OrderStatus orderStatus, OrderCriteria criteria) {
        LocalDateTime formDateTime = Optional.ofNullable(fromDate).map(LocalDate::atStartOfDay).orElse(null);
        LocalDateTime toDateTime = Optional.ofNullable(toDate).map(localDate -> toDate.atTime(hour, minute)).orElse(null);
        String code = "";
        if(type != null && type.equals("delivery")) {
            code = Optional.ofNullable(deliveryStatus).map(Delivery.DeliveryStatus::getCode).orElse(null);
        } else if(type != null && type.equals("order")) {
            code = Optional.ofNullable(orderStatus).map(Orders.OrderStatus::getCode).orElse(null);
        }
        List<Orders> ordersList = orderMapper.findBySellerId(userId, formDateTime, toDateTime, type,  code, criteria);
        return ordersList;
    }

    public List<Orders> findOrders(LocalDate fromDate, LocalDate toDate) {
        LocalDateTime formDateTime = Optional.ofNullable(fromDate).map(LocalDate::atStartOfDay).orElse(null);
        LocalDateTime toDateTime = Optional.ofNullable(toDate).map(localDate -> toDate.atTime(hour, minute)).orElse(null);
        List<Orders> ordersList = orderMapper.findAll(formDateTime, toDateTime);
        return ordersList;
    }

    public Orders findOrderByDeliveryId(Long deliveryId) {
        return orderMapper.findOneByDeliveryId(deliveryId);
    }

    @Transactional
    public Long order(OrderForm orderForm) {

        //임시 주문상태를 실제 주문 상태로 변경
        Orders order = orderMapper.findOne(orderForm.getOrderId()).get();
        order.setStatus(Orders.OrderStatus.DELIVERY);

        //결제 내용 추가
        Payment payment = orderForm.getPayment();
        Long paymentId = paymentService.save(payment);

        //배송 내용 추가
        Delivery delivery = orderForm.getDelivery();
        delivery.setStatus(Delivery.DeliveryStatus.WAIT);
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
        orderMapper.updateOrder(order.getOrderId(),order.getStatus(),deliveryId,paymentId);

        return order.getOrderId();
    }

    private void updateOrderStatus(Long orderId, Orders.OrderStatus orderStatus) {
        orderMapper.updateOrder(orderId, orderStatus, null, null);
    }

    //상품 교환 or 환불 시 상태변경 및 택배 요청
    @Transactional
    public Long updateOrderByRefundOrChangeRequest(Long orderId, Orders.OrderStatus orderStatus) {
        Optional<Orders> one = orderMapper.findOne(orderId);
        //Todo : not found exception 처리로 수정
        if(one.isEmpty() || !one.get().getOrderId().equals(orderId)) {
           return null;
        }
        Orders orders = one.get();
        if(orderStatus.equals(Orders.OrderStatus.REFUND)) {
            paymentService.cancelPayment(orders.getPayment());
        };
        Delivery delivery = orders.getDelivery();
        delivery.setStatus(Delivery.DeliveryStatus.WAIT);
        //Todo : 택배사에게 수거 요청 (to, from)
        deliveryService.update(delivery);
        //Todo : 캡슐화
        updateOrderStatus(orderId, orderStatus);
        return orderId;
    }

    /*
    //상품환불 확정 시
    @Transactional
    public Long updateOrderAfterCheckingRefund(Long orderId) {
        // 주문 가져오기
        Orders order = orderMapper.findOne(orderId).get();

        // 받은 상품 개수에 반환된 물품개수 추가
        List<OrderItem> orderItems = order.getOrderItems();
        List<ProductOption> productOptionList = new ArrayList<>();
        for(OrderItem orderItem : orderItems) {
            ProductOption productOption = orderItem.getProductOption();
            productOption.addStock(orderItem.getCount());
            productOptionList.add(productOption);
        }
        productService.updateProductOption(productOptionList);
        //Todo : 상품 환불 확정 시, 택배비 동봉 or 환불금액에서 차감 등의 기능을 넣어야함
        paymentService.cancelPayment(order.getPayment());
        // 다시 보내야할 상품 개수를 통해 물품개수 감소


        return orderId;
    }

    //상품교환 확정 시
    @Transactional
    public Long updateOrderAfterCheckingChange(Long orderId) {
        // 주문 가져오기
        Orders order = orderMapper.findOne(orderId).get();

        // 받은 상품 개수에 반환된 물품개수 추가
        List<OrderItem> orderItems = order.getOrderItems();
        List<ProductOption> productOptionList = new ArrayList<>();
        for(OrderItem orderItem : orderItems) {
            ProductOption productOption = orderItem.getProductOption();
            productOption.addStock(orderItem.getCount());
            productOptionList.add(productOption);
        }
        productService.updateProductOption(productOptionList);

        // 다시 보내야할 상품 개수를 통해 물품개수 감소


        return orderId;
    }
    */

    //주문 취소 (제품 보내기 전 결제 취소 시)
    @Transactional
    public Long cancelOrder(Long orderId) {

        // 주문 가져오기
        Orders order = orderMapper.findOne(orderId).get();

        //결제 취소
        Long paymentId = order.getPayment().getPaymentId();
        Payment payment = paymentService.findPayment(paymentId);
        String vendorCheckNumber = payment.getVendorCheckNumber();

        //실제 모듈 적용시
//        HttpHeaders headers = new HttpHeaders();
//        HttpEntity entity = new HttpEntity(headers);
//        URI payModuleURI = UriComponentsBuilder.fromHttpUrl(payModuleUrl + "/" + vendorCheckNumber).build().toUri();
//        ResponseEntity<String> response = restTemplate.exchange(payModuleURI, HttpMethod.DELETE, entity, String.class);

        // 테스트용 모듈
        ResponseEntity<String> response = moduleController.cancelPay(vendorCheckNumber);
        if(!response.getStatusCode().is2xxSuccessful()) {
            //재 전송 RetryTemplate 같은걸 사용 예정
            new RuntimeException("결제모듈 오류");
        }
        payment.setStatus(Payment.PaymentStatus.CANCEL);
        paymentService.cancelPayment(payment);

        // 주문 상태 변경
        order.setStatus(Orders.OrderStatus.CANCEL);

        // 물품 수량 변경
        List<OrderItem> orderItems = order.getOrderItems();
        List<ProductOption> productOptionList = new ArrayList<>();
        for(OrderItem orderItem : orderItems) {
            ProductOption productOption = orderItem.getProductOption();
            productOption.addStock(orderItem.getCount());
            productOptionList.add(productOption);
        }
        productService.updateProductOption(productOptionList);

        //주문상태 수정
        orderMapper.updateOrder(order.getOrderId(), order.getStatus(),null,null);

        // 결제 취소
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
        // 1. 상품조회
        Product product = productService.findProduct(tempOrderForm.getProductId());
        // 2. 주문생성
        List<OrderItem> orderItems = this.setOrderItemsInfo(tempOrderForm, product);

        // 3. 주문자 및 구매자 정보 조회
        User buyer = userService.getUser(tempOrderForm.getBuyerId());
        User seller = userService.getUser(tempOrderForm.getSellerId());

        //임시 주문 생성 (주문상품, 구매자, 판매자)
        Orders tempOrder = Orders.createTempOrder(orderItems,buyer,seller);

        return tempOrder;
    }

    /**
     * set Product, ProductOption Information
     * @param tempOrderForm
     * @return List<OrderItem>
     */
    private List<OrderItem> setOrderItemsInfo(TempOrderForm tempOrderForm, Product product){

        // Json To OrderItem
        List<OrderItem> orderItems = this.setJsonToOrderItem(tempOrderForm.getOrderItems());

        // 가져온 상품정보를 이용해 상품 옵션의 필요내용 설정
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
        List<OrderItem> items = null;

        try {
            items = Arrays.asList(objectMapper.readValue(orderItems, OrderItem[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }



}
