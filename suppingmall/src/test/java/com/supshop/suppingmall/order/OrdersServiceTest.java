package com.supshop.suppingmall.order;

import com.supshop.suppingmall.order.form.TempOrderForm;
import com.supshop.suppingmall.payment.PaymentService;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class OrdersServiceTest {

    @Autowired private OrderService orderService;
    @Autowired private PaymentService paymentService;
    @Autowired private OrderFactory orderFactory;
    @Autowired private UserFactory userFactory;

    //주문확인 테스트
    @Test
    @Transactional
    public void getOrder() throws Exception {
        //given
        Orders orders = orderFactory.buildOrder();

        //when
        Orders findOrder = orderService.getOrder(orders.getOrderId());


        //then
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
    @Transactional
    public void createTempOrder(){
        //given
        int size = orderService.getOrderList(null, null, null, null).size();
        User tester = userFactory.createUser("tester");
        TempOrderForm tempOrderForm = orderFactory.buildTempOrderForm(tester);

        //when
        Orders tempOrder = orderService.createOrder(tempOrderForm);
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
    /*@Test
    @Transactional
    public void orderProduct() throws Exception {
        //given
        OrderForm orderForm = orderFactory.buildOrderForm();

        //when
        Orders order = orderService.getTempOrder(orderForm);
        Long orderId = orderService.order(order);
        Orders newOrder = orderService.getOrder(orderId);

        assertEquals(newOrder.getStatus(), Orders.OrderStatus.DELIVERY);
        assertEquals(newOrder.getPayment().getPaymentType(), order.getPayment().getPaymentType());
        assertEquals(newOrder.getPayment().getPrice(), order.getPayment().getPrice());
        assertEquals(newOrder.getPayment().getStatus(), order.getPayment().getStatus());
        assertEquals(newOrder.getDelivery().getAddress(), order.getDelivery().getAddress());
        assertEquals(newOrder.getDelivery().getAddressDetail(), order.getDelivery().getAddressDetail());
        assertEquals(newOrder.getDelivery().getPhone(), order.getDelivery().getPhone());
        assertEquals(newOrder.getDelivery().getVendor(), order.getDelivery().getVendor());
        assertEquals(newOrder.getOrderItems().get(0).getProductOption().getQuantity(), order.getOrderItems().get(0).getProductOption().getQuantity());

    }*/



    /*@Test
    @Transactional
    public void cancelOrder() throws Exception {

        //given
        Orders orders = orderFactory.buildOrder();
        OrderItem originOrderItem = orders.getOrderItems().get(0);

        //when
        orderService.cancelOrder(orders.getOrderId());
        Orders order = orderService.getOrder(orders.getOrderId());
        Long paymentId = order.getPayment().getPaymentId();
        Payment payment = paymentService.findPayment(paymentId);
        OrderItem orderItem = order.getOrderItems().get(0);


        //then
        assertEquals(Orders.OrderStatus.CANCEL,order.getStatus());
        assertEquals(Payment.PaymentStatus.CANCEL,payment.getStatus());
        assertEquals(originOrderItem.getProductOption().getQuantity() + orderItem.getCount(), orderItem.getProductOption().getQuantity());


    }*/

    //Todo : order 상태 변경에 대한 해당 조건에 맞는 테스트 세분화
    /*@Test
    @Transactional
    public void changeOrderStatus() throws Exception {
        //given
        Orders orders = orderFactory.buildOrder();

        //when
        orderService.updateOrderByRefundOrChangeRequest(orders.getOrderId(), Orders.OrderStatus.REFUND);
        Orders changedOrder = orderService.getOrder(orders.getOrderId());

        //then
        assertEquals(Orders.OrderStatus.REFUND, changedOrder.getStatus());

    }*/




}