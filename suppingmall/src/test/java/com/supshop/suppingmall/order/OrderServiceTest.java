package com.supshop.suppingmall.order;

import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.payment.Payment;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductOption;
import com.supshop.suppingmall.product.ProductService;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Test
    @Transactional
    public void orderProduct() throws Exception {
        //given

            //유저정보
        User user = userService.getUser(19l);
            //배송입력정보
        Delivery delivery = Delivery.builder()
                .address(user.getAddress())
                .address(user.getAddressDetail())
                .zipCode(user.getZipCode())
                .status("배송준비중")
                .build();

        //상품정보
        List<OrderItem> orderItems = new ArrayList<>();

        Product product = productService.retrieveProduct(23l);
        List<ProductOption> options = product.getOptions();
        ProductOption productOption = options.get(1);


        int orderPrice = productOption.getPrice() * 2;
        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .productOption(productOption)
                .count(2)
                .orderPrice(orderPrice)
                .build();

        orderItems.add(orderItem);

            // 결제정보
        Payment payment = Payment.builder()
                .paymentType(Payment.PayGroup.CARD)
                .vendor(Payment.CardVendor.HYUNDAI)
                .price(orderPrice)
                .status(Payment.PaymentStatus.COMPLETE)
                .installmentMonth(1)
                .payedDate(LocalDateTime.now())
                .build();


        Order order = Order.builder()
                .orderItems(orderItems)
                .buyer(user)
                .delivery(delivery)
                .payment(payment)
                .build();

        //when
        Long orderId = orderService.createOrder(order);
        Order order1 = orderService.retrieveOrder(orderId);

        //then
        assertThat(order1.getOrderId()).isEqualTo(order.getOrderId());
        assertThat(order1.getStatus()).isEqualTo(order.getStatus());
        assertThat(order1.getPayment().getPaymentId()).isEqualTo(order.getPayment().getPaymentId());
        assertThat(order1.getPayment().getPaymentType()).isEqualTo(order.getPayment().getPaymentType());
        assertThat(order1.getPayment().getPrice()).isEqualTo(order.getPayment().getPrice());
        assertThat(order1.getPayment().getPayedDate()).isEqualTo(order.getPayment().getPayedDate());
        for(OrderItem orderNewItem : order1.getOrderItems()) {
            assertThat(orderNewItem.getCount()).isEqualTo(orderItem.getCount());
            assertThat(orderNewItem.getOrderPrice()).isEqualTo(orderItem.getOrderPrice());
            assertThat(orderNewItem.getProduct().getProductId()).isEqualTo(product.getProductId());
            assertThat(orderNewItem.getProductOption().getOptionId()).isEqualTo(productOption.getOptionId());
        }


    }

}