package com.supshop.suppingmall.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.delivery.DeliveryService;
import com.supshop.suppingmall.mapper.OrderItemMapper;
import com.supshop.suppingmall.mapper.OrderMapper;
import com.supshop.suppingmall.payModule.ModuleController;
import com.supshop.suppingmall.payment.Payment;
import com.supshop.suppingmall.payment.PaymentService;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductOption;
import com.supshop.suppingmall.product.ProductService;
import com.supshop.suppingmall.user.UserService;
import com.supshop.suppingmall.user.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
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
    private static final String payModuleUrl = "/payModule";
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final ModuleController moduleController;

    public Orders findOrder(Long orderId) {
        Optional<Orders> order = orderMapper.findOne(orderId);
        return order.orElseThrow(NoSuchElementException::new);
    }

    //구매자의 관점에서 주문을 조회
    public List<Orders> findOrderByBuyerId(Long userId, LocalDate fromDate, LocalDate toDate, String type, Orders.OrderStatus status) {
        LocalDateTime formDateTime = Optional.ofNullable(fromDate).map(LocalDate::atStartOfDay).orElse(null);
        LocalDateTime toDateTime = Optional.ofNullable(toDate).map(localDate -> toDate.atTime(23, 59)).orElse(null);
        String code = Optional.ofNullable(status).map(Orders.OrderStatus::getCode).orElse(null);
        List<Orders> ordersList = orderMapper.findByBuyerId(userId, formDateTime, toDateTime, type,  code);
        return ordersList;
    }

    //판매자의 관점에서 주문을 조회
    public List<Orders> findOrderBySellerId(Long userId, LocalDate fromDate, LocalDate toDate, String type, Delivery.DeliveryStatus status) {
        LocalDateTime formDateTime = Optional.ofNullable(fromDate).map(LocalDate::atStartOfDay).orElse(null);
        LocalDateTime toDateTime = Optional.ofNullable(toDate).map(localDate -> toDate.atTime(23, 59)).orElse(null);
        String code = Optional.ofNullable(status).map(Delivery.DeliveryStatus::getCode).orElse(null);
        List<Orders> ordersList = orderMapper.findBySellerId(userId, formDateTime, toDateTime, type,  code);
        return ordersList;
    }

    public List<Orders> findOrders(LocalDate fromDate, LocalDate toDate) {
        LocalDateTime formDateTime = Optional.ofNullable(fromDate).map(LocalDate::atStartOfDay).orElse(null);
        LocalDateTime toDateTime = Optional.ofNullable(toDate).map(localDate -> toDate.atTime(23, 59)).orElse(null);
        List<Orders> ordersList = orderMapper.findAll(formDateTime, toDateTime);
        return ordersList;
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
        orderMapper.updateOrder(order.getOrderId(),order.getStatus(),deliveryId,paymentId);

        return order.getOrderId();
    }

    @Transactional
    public Long cancelOrder(Long orderId) {

        // 주문 가져오기
        Orders order = orderMapper.findOne(orderId).get();

        //결제 취소
        Long paymentId = order.getPayment().getPaymentId();
        Payment payment = paymentService.findPayment(paymentId);
        String vendorCheckNumber = payment.getVendorCheckNumber();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity entity = new HttpEntity(headers);
//        URI payModuleURI = UriComponentsBuilder.fromHttpUrl(payModuleUrl + "/" + vendorCheckNumber).build().toUri();
//        ResponseEntity<String> response = restTemplate.exchange(payModuleURI, HttpMethod.DELETE, entity, String.class);
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
        Product product = productService.findProduct(tempOrderForm.getProductId());

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
