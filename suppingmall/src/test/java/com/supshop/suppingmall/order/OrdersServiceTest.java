package com.supshop.suppingmall.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.payment.CardVO;
import com.supshop.suppingmall.payment.Payment;
import com.supshop.suppingmall.payment.PaymentService;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductOption;
import com.supshop.suppingmall.product.ProductService;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserService;
import com.supshop.suppingmall.user.UserVO;
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
        Orders tempOrder = orderService.createTempOrder(tempOrderForm);


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
                .price(orderPrice)
                .build();

        orderItems.add(orderItem);

        CardVO card = CardVO.builder().vendor(Payment.CardVendor.HYUNDAI).installmentMonth(1).cardNumber("000-000-000").build();
        // 결제정보
        Payment payment = Payment.builder()
                .paymentType(Payment.PayGroupType.CARD)
                .card(card)
                .price(orderPrice)
                .status(Payment.PaymentStatus.COMPLETE)
                .payedDate(LocalDateTime.now())
                .build();


        Orders orders = Orders.builder()
                .orderItems(orderItems)
                .buyer(userVO)
                .delivery(delivery)
                .payment(payment)
                .build();

        //when



        Long orderId = orderService.order(orders);
        Orders orders1 = orderService.retrieveOrder(orderId);

        //then
        assertThat(orders1.getOrderId()).isEqualTo(orders.getOrderId());
        assertThat(orders1.getStatus()).isEqualTo(orders.getStatus());
        assertThat(orders1.getPayment().getPaymentId()).isEqualTo(orders.getPayment().getPaymentId());
        assertThat(orders1.getPayment().getPaymentType()).isEqualTo(orders.getPayment().getPaymentType());
        assertThat(orders1.getPayment().getPrice()).isEqualTo(orders.getPayment().getPrice());
        assertThat(orders1.getPayment().getPayedDate()).isEqualTo(orders.getPayment().getPayedDate());
        for(OrderItem orderNewItem : orders1.getOrderItems()) {
            assertThat(orderNewItem.getCount()).isEqualTo(orderItem.getCount());
            assertThat(orderNewItem.getPrice()).isEqualTo(orderItem.getPrice());
            assertThat(orderNewItem.getProduct().getProductId()).isEqualTo(product.getProductId());
            assertThat(orderNewItem.getProductOption().getOptionId()).isEqualTo(productOption.getOptionId());
        }


    }

}