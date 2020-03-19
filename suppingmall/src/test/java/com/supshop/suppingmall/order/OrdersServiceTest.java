package com.supshop.suppingmall.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.payment.Payment;
import com.supshop.suppingmall.payment.PaymentService;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductOption;
import com.supshop.suppingmall.product.ProductService;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserService;
import com.supshop.suppingmall.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    PaymentService paymentService;

    @Test
    @Transactional
    public void getOrder() throws Exception {
        //given

        //상품정보
        List<OrderItemForm> orderItems = new ArrayList<>();

        Product product = productService.retrieveProduct(23l);
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
        UserVO userVO = userService.getUserVO(buyerId);


        //then
        assertThat(tempOrder.getStatus()).isEqualTo(Orders.OrderStatus.WAIT);
        assertThat(tempOrder.getBuyer().getUserId()).isEqualTo(buyerId);
        assertThat(tempOrder.getBuyer().getNickName()).isEqualTo(userVO.getNickName());
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

        Product product = productService.retrieveProduct(23l);
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
        UserVO userVO = UserVO.builder().userId(user.getUserId()).build();

        Orders order = orderService.findOrder(7l);

        List<OrderItem> orderItemList = order.getOrderItems();
        for(OrderItem orderItem : orderItemList) {
            ProductOption productOption = orderItem.getProductOption();
            productOption.removeStock(orderItem.getCount());
        }


        //배송입력정보
        Delivery delivery = Delivery.builder()
                .name(user.getName())
                .address(user.getAddress())
                .address(user.getAddressDetail())
                .zipCode(user.getZipCode())
                .phone(user.getPhoneNumber())
                .vendor("대한통운")
                .build();


        // 결제정보
        Payment payment = Payment.builder()
                .paymentType(Payment.PayGroupType.CARD)
                .price(order.getAmountPrice())
                .status(Payment.PaymentStatus.COMPLETE)
                .payDate(LocalDateTime.now())
                .build();

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

}