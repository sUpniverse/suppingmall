package com.supshop.suppingmall.order;

import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.order.form.OrderForm;
import com.supshop.suppingmall.order.form.TempOrderForm;
import com.supshop.suppingmall.payment.Payment;
import com.supshop.suppingmall.payment.PaymentService;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductService;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrdersServiceTest {

    @Autowired private OrderService orderService;
    @Autowired private PaymentService paymentService;
    @Autowired private OrderFactory orderFactory;
    @Autowired private UserFactory userFactory;
    @Autowired private ProductService productService;
    @Autowired private OrderItemService orderItemService;

    //주문확인 테스트
    @Test
    public void getOrder() throws Exception {
        //given
        Orders orders = orderFactory.createOrder();

        //when
        Orders findOrder = orderService.getOrder(orders.getOrderId());

        //then
        assertThat(findOrder.getStatus()).isEqualTo(Orders.OrderStatus.DELIVERY);
        assertThat(findOrder.getAmountProductCount()).isEqualTo(orders.getAmountProductCount());
        assertThat(findOrder.getAmountProductPrice()).isEqualTo(orders.getAmountProductPrice());
        for(int i = 0; i < findOrder.getOrderItems().size(); i++) {
            assertThat(findOrder.getOrderItems().get(i).getCount()).isEqualTo(orders.getOrderItems().get(i).getCount());
            assertThat(findOrder.getOrderItems().get(i).getProduct().getProductId()).isEqualTo(orders.getOrderItems().get(i).getProduct().getProductId());
            assertThat(findOrder.getOrderItems().get(i).getProductOption().getOptionId()).isEqualTo(orders.getOrderItems().get(i).getProductOption().getOptionId());
        }

    }

    //임시 주문 생성 테스트
    @Test
    public void createTempOrder(){
        //given
        int size = orderService.getOrderList(null, null, null, null).size();
        User tester = userFactory.createUser("tester");
        TempOrderForm tempOrderForm = orderFactory.buildTempOrderForm(tester);

        //when
        Orders tempOrder = orderService.createTempOrder(tempOrderForm);
        Orders orderNewItem = orderService.getOrder(tempOrder.getOrderId());
        int addedSize = orderService.getOrderList(null, null, null, null).size();

        //then
        assertThat(size).isEqualTo(addedSize);
        assertThat(orderNewItem.getStatus()).isEqualTo(Orders.OrderStatus.WAIT);
        assertThat(orderNewItem.getAmountProductCount()).isEqualTo(tempOrder.getAmountProductCount());
        assertThat(orderNewItem.getAmountProductPrice()).isEqualTo(tempOrder.getAmountProductPrice());
        for(int i = 0; i < tempOrder.getOrderItems().size(); i++) {
            assertThat(orderNewItem.getOrderItems().get(i).getCount()).isEqualTo(tempOrder.getOrderItems().get(i).getCount());
            assertThat(orderNewItem.getOrderItems().get(i).getProduct().getProductId()).isEqualTo(tempOrder.getOrderItems().get(i).getProduct().getProductId());
            assertThat(orderNewItem.getOrderItems().get(i).getProductOption().getOptionId()).isEqualTo(tempOrder.getOrderItems().get(i).getProductOption().getOptionId());
        }
    }



    // 임시주문 -> 주문완료 테스트
    @Test
    public void orderProduct() throws Exception {
        //given
        OrderForm orderForm = orderFactory.buildOrderForm();
        Orders order = orderService.getOrder(orderForm.getOrderId());
        Product beforeOrderProduct = productService.getProduct(order.getOrderItems().get(0).getProduct().getProductId());


        //when
        orderService.order(order,orderForm.getPayment(),orderForm.getDelivery());
        Orders newOrder = orderService.getOrder(order.getOrderId());
        Product afterOrderProduct = productService.getProduct(order.getOrderItems().get(0).getProduct().getProductId());
        OrderItem orderItem = order.getOrderItems().get(0);
        OrderItem newOrderItem = newOrder.getOrderItems().get(0);

        //then
        assertEquals(Orders.OrderStatus.COMPLETE,newOrder.getStatus());
        assertEquals(orderItem.getProductOption().getPrice(),newOrderItem.getPayment().getPrice());
        assertEquals(orderForm.getDelivery().getAddress(),newOrderItem.getDelivery().getAddress());
        assertEquals(orderItem.getProductOption().getQuantity(),newOrderItem.getProductOption().getQuantity());
        // 주문전 수량 - 주문한 수량  = 현재 수량
        assertEquals(beforeOrderProduct.getOptions().get(orderItem.getProductOption().getOptionId()-1).getQuantity() - order.getOrderItems().get(0).getCount(),afterOrderProduct.getOptions().get(orderItem.getProductOption().getOptionId()-1).getQuantity());


    }

    @Test
    public void cancelOrder() throws Exception {

        //given
        Orders orders = orderFactory.createOrder();
        OrderItem originOrderItem = orders.getOrderItems().get(0);
        Product beforeOrderProduct = productService.getProduct(originOrderItem.getProduct().getProductId());

        //when
        orderItemService.cancelOrderItem(originOrderItem);
        Long paymentId = originOrderItem.getPayment().getPaymentId();
        OrderItem orderItem = orderItemService.getOrderItem(originOrderItem.getOrderItemId());
        Payment payment = paymentService.findPayment(paymentId);
        Product afterOrderProduct = productService.getProduct(orderItem.getProduct().getProductId());


        //then
        assertEquals(Orders.OrderStatus.CANCEL,orderItem.getStatus());
        assertEquals(Payment.PaymentStatus.CANCEL,payment.getStatus());
        // 취소전 수량 + 취소 수량  = 현재 수량
        assertEquals(beforeOrderProduct.getOptions().get(orderItem.getProductOption().getOptionId()-1).getQuantity() + originOrderItem.getCount(),afterOrderProduct.getOptions().get(orderItem.getProductOption().getOptionId()-1).getQuantity());


    }

    @Test
    @Transactional
    public void changeOrderStatusBySeller() throws Exception {
        //given
        Orders orders = orderFactory.createOrder();
        OrderItem originOrderItem = orders.getOrderItems().get(0);

        //when
        orderItemService.changeStatus(originOrderItem, Orders.OrderStatus.DELIVERY);
        OrderItem orderItem = orderItemService.getOrderItem(originOrderItem.getOrderItemId());

        //then
        assertEquals(Orders.OrderStatus.DELIVERY, orderItem.getStatus());
        assertEquals(Delivery.DeliveryStatus.WAIT, orderItem.getDelivery().getStatus());

    }




}