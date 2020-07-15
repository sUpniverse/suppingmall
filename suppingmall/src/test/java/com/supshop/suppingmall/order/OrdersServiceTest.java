package com.supshop.suppingmall.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.order.Form.OrderForm;
import com.supshop.suppingmall.order.Form.TempOrderForm;
import com.supshop.suppingmall.payment.Payment;
import com.supshop.suppingmall.payment.PaymentService;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductOption;
import com.supshop.suppingmall.product.ProductService;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserService;
import com.supshop.suppingmall.user.SessionUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrdersServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    @Transactional
    public void getOrder() throws Exception {
        //given

        //상품정보
        List<OrderItemForm> orderItems = new ArrayList<>();

        Product product = productService.findProduct(23l);
        Product newProduct = Product.builder().productId(23l).build();

        List<ProductOption> options = new ArrayList<>();
        ProductOption productOption = product.getOptions().get(1);
        options.add(productOption);

        int count = 2;
        OrderItemForm orderItemForm = OrderItemForm.builder()
                .product(newProduct)
                .productOption(productOption)
                .count(count)
                .price(productOption.getPrice())
                .build();
        orderItems.add(orderItemForm);

        TempOrderForm tempOrderForm = new TempOrderForm();
        long buyerId = 19l;
        tempOrderForm.setBuyerId(buyerId);
        long sellerId = 18l;
        tempOrderForm.setSellerId(sellerId);
        ObjectMapper objectMapper = new ObjectMapper();
        tempOrderForm.setOrderItems(objectMapper.writeValueAsString(orderItems));
        Long tempOrderId = orderService.createOrder(tempOrderForm).getOrderId();

        //when
        Orders tempOrder = orderService.findOrder(tempOrderId);
        SessionUser sessionUser = userService.getUserVO(buyerId);


        //then
        assertThat(tempOrder.getStatus()).isEqualTo(Orders.OrderStatus.WAIT);
        assertThat(tempOrder.getBuyer().getUserId()).isEqualTo(buyerId);
        assertThat(tempOrder.getBuyer().getNickName()).isEqualTo(sessionUser.getNickName());
        assertThat(tempOrder.getSeller().getUserId()).isEqualTo(sellerId);

        for(OrderItem orderNewItem : tempOrder.getOrderItems()) {
            assertThat(orderNewItem.getCount()).isEqualTo(count);
            assertThat(orderNewItem.getPrice()).isEqualTo(productOption.getPrice());
            assertThat(orderNewItem.getProduct().getProductId()).isEqualTo(product.getProductId());
            assertThat(orderNewItem.getProduct().getName()).isEqualTo(product.getName());
            assertThat(orderNewItem.getProduct().getPrice()).isEqualTo(product.getPrice());
            assertThat(orderNewItem.getProduct().getThumbnail()).isEqualTo(product.getThumbnail());
            assertThat(orderNewItem.getProductOption().getOptionId()).isEqualTo(productOption.getOptionId());
            assertThat(orderNewItem.getProductOption().getOptionName()).isEqualTo(productOption.getOptionName());
            assertThat(orderNewItem.getProductOption().getPrice()).isEqualTo(productOption.getPrice());
            assertThat(orderNewItem.getProductOption().getQuantity()).isEqualTo(productOption.getQuantity());
        }

    }

    @Test
    @Transactional
    public void createTempOrder() throws Exception {
        //given

        //상품정보
        List<OrderItemForm> orderItems = new ArrayList<>();

        Product product = productService.findProduct(23l);
        Product newProduct = Product.builder().productId(23l).build();

        List<ProductOption> options = new ArrayList<>();
        ProductOption productOption = product.getOptions().get(1);
        options.add(productOption);

        int count = 2;
        OrderItemForm orderItemForm = OrderItemForm.builder()
                .product(newProduct)
                .productOption(productOption)
                .count(count)
                .price(productOption.getPrice())
                .build();
        orderItems.add(orderItemForm);

        TempOrderForm tempOrderForm = new TempOrderForm();
        long buyerId = 19l;
        tempOrderForm.setBuyerId(buyerId);
        tempOrderForm.setSellerId(product.getSeller().getUserId());
        ObjectMapper objectMapper = new ObjectMapper();
        tempOrderForm.setOrderItems(objectMapper.writeValueAsString(orderItems));

        //when
        Orders tempOrder = orderService.createOrder(tempOrderForm);


        //then
        assertThat(tempOrder.getStatus()).isEqualTo(Orders.OrderStatus.WAIT);

        for(OrderItem orderNewItem : tempOrder.getOrderItems()) {
            assertThat(orderNewItem.getCount()).isEqualTo(count);
            assertThat(orderNewItem.getPrice()).isEqualTo(productOption.getPrice());
            assertThat(orderNewItem.getProduct().getProductId()).isEqualTo(product.getProductId());
            assertThat(orderNewItem.getProductOption().getOptionId()).isEqualTo(productOption.getOptionId());
        }

    }

    @Test
    @Transactional
    public void orderProduct() throws Exception {
        //given

        //유저정보
        User user = userService.getUser(19l);
        SessionUser sessionUser = SessionUser.builder().userId(user.getUserId()).build();

        Orders order = orderService.findOrder(7l);

        List<OrderItem> orderItemList = order.getOrderItems();
        for(OrderItem orderItem : orderItemList) {
            ProductOption productOption = orderItem.getProductOption();
            productOption.removeStock(orderItem.getCount());
        }


        //배송입력정보
        Delivery delivery = buildDelivery(user);


        // 결제정보
        Payment payment = buildPayment(order);

        OrderForm orderForm = new OrderForm();
        orderForm.setDelivery(delivery);
        orderForm.setPayment(payment);
        orderForm.setOrderId(order.getOrderId());

        //when
        Long orderId = orderService.order(orderForm);
        Orders newOrder = orderService.findOrder(orderId);

        assertEquals(newOrder.getOrderId(), orderId);
        assertEquals(newOrder.getStatus(), Orders.OrderStatus.ORDER);
        assertEquals(newOrder.getPayment().getPaymentType(), payment.getPaymentType());
        assertEquals(newOrder.getPayment().getPrice(), payment.getPrice());
        assertEquals(newOrder.getPayment().getStatus(), payment.getStatus());
        assertEquals(newOrder.getDelivery().getAddress(), delivery.getAddress());
        assertEquals(newOrder.getDelivery().getAddressDetail(), delivery.getAddressDetail());
        assertEquals(newOrder.getDelivery().getPhone(), delivery.getPhone());
        assertEquals(newOrder.getDelivery().getMemo(), delivery.getMemo());
        assertEquals(newOrder.getDelivery().getVendor(), delivery.getVendor());
        assertEquals(newOrder.getOrderItems().get(0).getProductOption().getQuantity(), order.getOrderItems().get(0).getProductOption().getQuantity());

    }



    @Test
    @Transactional
    public void cancelOrder() throws Exception {

        //given
        Orders orders = buildOrder();
        OrderItem originOrderItem = orders.getOrderItems().get(0);


        //when
        orderService.cancelOrder(orders.getOrderId());
        Orders order = orderService.findOrder(orders.getOrderId());
        Long paymentId = order.getPayment().getPaymentId();
        Payment payment = paymentService.findPayment(paymentId);
        OrderItem orderItem = order.getOrderItems().get(0);


        //then
        assertEquals(Orders.OrderStatus.CANCEL,order.getStatus());
        assertEquals(Payment.PaymentStatus.CANCEL,payment.getStatus());
        assertEquals(originOrderItem.getProductOption().getQuantity() + orderItem.getCount(), orderItem.getProductOption().getQuantity());


    }

    @Test
    @Transactional
    public void changeOrderStatus() throws Exception {
        //given
        Orders orders = buildOrder();

        //when
        orderService.updateOrderStatus(orders.getOrderId(), Orders.OrderStatus.REFUND);
        Orders changedOrder = orderService.findOrder(orders.getOrderId());

        //then
        assertEquals(Orders.OrderStatus.REFUND, changedOrder.getStatus());

    }

    private Orders buildOrder() throws JsonProcessingException {
        //임시상품정보
        Long tempOrderId = buildTempOrder();

        //유저정보
        User user = userService.getUser(19l);
        SessionUser sessionUser = SessionUser.builder().userId(user.getUserId()).build();

        Orders order = orderService.findOrder(tempOrderId);

        //배송입력정보
        Delivery delivery = buildDelivery(user);

        // 결제정보
        Payment payment = buildPayment(order);

        //실제 주문
        order(order, delivery, payment);

        return order;
    }

    private Long order(Orders order, Delivery delivery, Payment payment) {
        OrderForm orderForm = new OrderForm();
        orderForm.setDelivery(delivery);
        orderForm.setPayment(payment);
        orderForm.setOrderId(order.getOrderId());
        Long orderId = orderService.order(orderForm);
        return orderId;
    }

    private Long buildTempOrder() throws JsonProcessingException {
        List<OrderItemForm> orderItems = new ArrayList<>();

        long productId = 23l;
        Product product = productService.findProduct(productId);
        Product newProduct = Product.builder().productId(productId).build();

        List<ProductOption> options = new ArrayList<>();
        ProductOption productOption = product.getOptions().get(1);
        options.add(productOption);

        int count = 2;
        OrderItemForm orderItemForm = OrderItemForm.builder()
                .product(newProduct)
                .productOption(productOption)
                .count(count)
                .price(productOption.getPrice())
                .build();
        orderItems.add(orderItemForm);

        TempOrderForm tempOrderForm = new TempOrderForm();
        long buyerId = 19l;
        tempOrderForm.setBuyerId(buyerId);
        long sellerId = 18l;
        tempOrderForm.setSellerId(sellerId);
        tempOrderForm.setProductId(productId);

        tempOrderForm.setOrderItems(objectMapper.writeValueAsString(orderItems));

        Long orderId = orderService.createOrder(tempOrderForm).getOrderId();

        return orderId;
    }

    private Payment buildPayment(Orders order) {

        return Payment.builder()
                .paymentType(Payment.PayGroupType.CARD)
                .price(order.getAmountPrice())
                .status(Payment.PaymentStatus.COMPLETE)
                .payDate(LocalDateTime.now())
                .vendorCheckNumber("vaf00")
                .build();
    }

    private Delivery buildDelivery(User user) {
        return Delivery.builder()
                .name(user.getName())
                .address(user.getAddress())
                .address(user.getAddressDetail())
                .zipCode(user.getZipCode())
                .phone(user.getPhoneNumber())
                .vendor(Delivery.DeliveryVendor.CJ)
                .build();
    }

}