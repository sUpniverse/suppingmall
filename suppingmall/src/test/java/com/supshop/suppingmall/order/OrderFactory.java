package com.supshop.suppingmall.order;

import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.delivery.DeliveryFactory;
import com.supshop.suppingmall.order.form.OrderForm;
import com.supshop.suppingmall.order.form.TempOrderForm;
import com.supshop.suppingmall.payment.Payment;
import com.supshop.suppingmall.payment.PaymentFactory;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductFactory;
import com.supshop.suppingmall.product.ProductService;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@Component
@ActiveProfiles("test")
public class OrderFactory {

    @Autowired private OrderService orderService;
    @Autowired private ProductService productService;
    @Autowired private DeliveryFactory deliveryFactory;
    @Autowired private PaymentFactory paymentFactory;
    @Autowired private UserFactory userFactory;
    @Autowired private ProductFactory productFactory;


    private Product setUpTest(String productName) {
        User seller = userFactory.createSeller("seller");
        seller.setCreatedDate(null);

        Product product = productFactory.createProduct(productName, seller);

        return product;
    }


    // 임시주문을 위한 form을 return, 구매할 물품들의 정보와 갯수 가격등을 포함함 , 물품 페이지에서 단일 물품을 구매할 때
    public TempOrderForm buildTempOrderForm(User user){
        Product product = setUpTest("notebook");

        List<OrderItem> orderItems = new ArrayList<>();

        int count = 2;
        for(int i = 0; i < product.getOptions().size(); i++) {
            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .productOption(product.getOptions().get(0))
                    .count(count)
                    .price(product.getOptions().get(0).getPrice())
                    .build();
            orderItems.add(orderItem);
        }

        TempOrderForm tempOrderForm = new TempOrderForm();

        tempOrderForm.setBuyerId(user.getUserId());
        tempOrderForm.setSellerId(product.getSeller().getUserId());
        tempOrderForm.setProductId(product.getProductId());
        tempOrderForm.setOrderItems(orderItems);

        return tempOrderForm;
    }

    // 임시주문을 위한 form을 return, 구매할 물품들의 정보와 갯수 가격등을 포함함 , 물품 페이지에서 다중 물품을 구매할 때
    public TempOrderForm buildTempOrderFormWithMoreThanTwoProduct(User user){
        Product product = setUpTest("notebook");
        Product product2 = setUpTest("tablet");

        List<OrderItem> orderItems = new ArrayList<>();

        int count = 2;
        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .productOption(product.getOptions().get(0))
                .count(count)
                .price(product.getOptions().get(0).getPrice())
                .build();
        orderItems.add(orderItem);

        OrderItem orderItem2 = OrderItem.builder()
                .product(product2)
                .productOption(product.getOptions().get(0))
                .count(count)
                .price(product.getOptions().get(0).getPrice())
                .build();
        orderItems.add(orderItem2);


        TempOrderForm tempOrderForm = new TempOrderForm();

        tempOrderForm.setBuyerId(user.getUserId());
        tempOrderForm.setSellerId(product.getSeller().getUserId());
        tempOrderForm.setProductId(product.getProductId());
        tempOrderForm.setOrderItems(orderItems);

        return tempOrderForm;
    }


    //임시주문을 가져와 배송정보와 결제정보등을 담은 form 상태 반환함
    public OrderForm buildOrderForm() {

        // 임시상품정보
        User user = userFactory.createUser("tester");
        TempOrderForm tempOrderForm = buildTempOrderForm(user);
        Orders order = orderService.createTempOrder(tempOrderForm);

        // 배송입력정보
        Delivery delivery = deliveryFactory.buildDelivery(user);

        // 결제정보
        Payment payment = paymentFactory.buildPaymentList(15000);

        OrderForm orderForm = new OrderForm();
        orderForm.setOrderId(order.getOrderId());
        orderForm.setDelivery(delivery);
        orderForm.setPayment(payment);

        return orderForm;
    }

    //임시주문을 가져와 배송정보와 결제정보등을 담은 form 상태 반환함
    public Orders createTempOrder() {

        // 임시상품정보
        User user = userFactory.createUser("tester");
        TempOrderForm tempOrderForm = buildTempOrderForm(user);
        Orders order = orderService.createTempOrder(tempOrderForm);

        return order;
    }


    //실제 주문이 완료된 상태의 주문 반환
    public Orders createOrder() throws Exception {
        OrderForm orderForm = this.buildOrderForm();
        Orders order = orderService.getOrder(orderForm.getOrderId());
        orderService.order(order,orderForm.getPayment(),orderForm.getDelivery());
        Orders newOrder = orderService.getOrder(order.getOrderId());
        return newOrder;
    }




}
