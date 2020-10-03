package com.supshop.suppingmall.order;

import com.supshop.suppingmall.common.UserUtils;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.order.form.OrderForm;
import com.supshop.suppingmall.order.form.TempOrderForm;
import com.supshop.suppingmall.page.TenItemsCriteria;
import com.supshop.suppingmall.page.PageMaker;
import com.supshop.suppingmall.user.Role;
import com.supshop.suppingmall.user.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping("/orders")
@RequiredArgsConstructor
@Controller
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    private static final int orderDisplayPagingNum = 5;

    @PostMapping("/orderSheet")
    @ResponseBody
    public ResponseEntity createOrderSheet(@RequestBody TempOrderForm tempOrderForm,
                                           @ModelAttribute(value = "orderForm") OrderForm orderForm) {

        // 임시주문
        Orders tempOrder = orderService.createTempOrder(tempOrderForm);
        URI link = linkTo(OrderController.class).slash("/orderSheet").slash(tempOrder.getOrderId()).toUri();

        // 반영된 주문 표시
        return ResponseEntity.created(link).build();
    }


    @PostMapping("")
    public String createOrder(OrderForm orderForm,
                              @AuthenticationPrincipal SessionUser user){

        Orders orders = null;
        try {
            orders = orderService.getOrder(orderForm.getOrderId());

            // 주문자와 현재 세션의 유저가 같지 않은지 && 이미 주문이 완료된 주문인지
            if(!UserUtils.isOwner(orders.getOrderItems().get(0).getBuyer().getUserId(),user) && orders.getStatus().equals(Orders.OrderStatus.ORDER)) {
                return "/order/fail";
            }

            orderService.order(orders,orderForm.getPayment(),orderForm.getDelivery());

        } catch (Exception e) {
            // 주문 실패
            // Todo : 주문의 실패 혹은 상품수량의 부족등 여러 가지 이유를 세분화 해서 에러처리
            return "/order/fail";
        }

        //성공
        return "/order/success";

    }

    @GetMapping("/orderSheet/{id}")
    public String getOrderSheet(@PathVariable Long id,
                                @AuthenticationPrincipal SessionUser user,
                                Model model) {

        Orders order = orderService.getOrder(id);
        if(!UserUtils.isOwner(order.getOrderItems().get(0).getBuyer().getUserId(), user)) {
            return "redirect:/products/";
        }

        // 반영된 주문 표시
        model.addAttribute("orderItems", order.getOrderItems());
        model.addAttribute("product",order.getOrderItems().get(0).getProduct());
        model.addAttribute("tempOrder",order);
        return "/order/form";
    }

    @GetMapping("")
    public String getOrders(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                            @RequestParam(required = false) Orders.OrderStatus orderStatus,
                            @AuthenticationPrincipal SessionUser user,
                            TenItemsCriteria criteria,
                            Model model) {


        int count = orderService.findCount("seller", user.getUserId());
        PageMaker pageMaker = new PageMaker(count, orderDisplayPagingNum, criteria);
        List<Orders> orders = orderService.getOrderList(fromDate,toDate,orderStatus,null);

        model.addAttribute("orders",orders);
        model.addAttribute("pageMaker",pageMaker);

        model.addAttribute("statusList", Arrays.asList(Orders.OrderStatus.ORDER,Orders.OrderStatus.DELIVERY,Orders.OrderStatus.COMPLETE,Orders.OrderStatus.CANCEL,Orders.OrderStatus.REFUND,Orders.OrderStatus.CHANGE));
        return "/order/admin/list";
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable Long id,
                           @AuthenticationPrincipal SessionUser user,
                           Model model) {
        Orders order = orderService.getOrder(id);
        model.addAttribute("order",order);
        if(user.getRole().equals(Role.SELLER)) {
            return "/order/seller/detail";
        } else if(user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.MASTER) ) {
            return "/order/seller/detail";
        }
        return "/order/detail";
    }

    @GetMapping("/orderItems/{id}")
    @ResponseBody
    public ResponseEntity getOrderItem(@PathVariable Long id,
                               @AuthenticationPrincipal SessionUser user) {
        OrderItem orderItem = orderItemService.getOrderItem(id);
        return ResponseEntity.ok(orderItem);
    }



    @GetMapping("/main")
    public String getOrdersByBuyerId(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                    @RequestParam(required = false) String type,
                                    @RequestParam(required = false) Orders.OrderStatus status,
                                    @AuthenticationPrincipal SessionUser user,
                                    Model model) {

        List<OrderItem> orderItemList = orderItemService.getOrderByBuyerId(user.getUserId(), fromDate, toDate, type, status);
        model.addAttribute("orderItemList",orderItemList);
        model.addAttribute("statusList", Arrays.asList(Orders.OrderStatus.values()));

        return "/order/list";
    }


    @GetMapping("/seller/main")
    public String getOrdersBySellerIdOnDelivery(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                     @RequestParam(required = false) String type,
                                     @RequestParam(required = false) Delivery.DeliveryStatus deliveryStatus,
                                     @RequestParam(required = false) Orders.OrderStatus orderStatus,
                                     @AuthenticationPrincipal SessionUser user,
                                     TenItemsCriteria criteria,
                                     Model model) {


        int count = orderService.findCount("seller", user.getUserId());
        PageMaker pageMaker = new PageMaker(count, orderDisplayPagingNum, criteria);
        List<Orders> pagingOrders = orderService.getOrderBySellerId(user.getUserId(),fromDate,toDate,type,deliveryStatus,orderStatus,criteria);

        model.addAttribute("orders",pagingOrders);
        model.addAttribute("pageMaker",pageMaker);

        // type이 order일시에는 환불관련 페이지로
        if(type != null && type.equals("order")) {
            model.addAttribute("statusList", Arrays.asList(Orders.OrderStatus.CANCEL,Orders.OrderStatus.REFUND,Orders.OrderStatus.CHANGE));
            return "/order/seller/refund-list";
        }

//        int wait = (int) pagingOrders.stream().filter(orders -> orders.getDelivery().getStatus().equals(Delivery.DeliveryStatus.WAIT)).count();
//        int delivery = (int) pagingOrders.stream().filter(orders -> orders.getDelivery().getStatus().equals(Delivery.DeliveryStatus.DELIVERY)).count();
//        int complete = (int) pagingOrders.stream().filter(orders -> orders.getDelivery().getStatus().equals(Delivery.DeliveryStatus.COMPLETE)).count();
//        int change = (int) pagingOrders.stream().filter(orders -> orders.getDelivery().getStatus().equals(Delivery.DeliveryStatus.CHANGE)).count();


//        model.addAttribute("status",Arrays.asList(wait,delivery,complete,change));
        model.addAttribute("statusList", Delivery.DeliveryStatus.values());

        return "/order/seller/list";
    }


    @GetMapping("/{id}/seller")
    public String getOrderBySellerId(@PathVariable Long id, Model model) {
        Orders order = orderService.getOrder(id);
        model.addAttribute("order",order);
        return "/order/seller/detail";
    }

    @PostMapping("/{id}/cancel")
    @ResponseBody
    public ResponseEntity cancelOrder(@PathVariable Long id,
                              @AuthenticationPrincipal SessionUser sessionUser) {
        OrderItem orderItem = orderItemService.getOrderItem(id);

        if(orderItem == null && !UserUtils.isOwner(orderItem.getBuyer().getUserId(),sessionUser)){
            new IllegalArgumentException();
            return ResponseEntity.badRequest().body("존재하지 않습니다.");
        }

        orderItem.getProductOption().setProductId(orderItem.getProduct().getProductId());
        orderItemService.cancelOrderItem(orderItem);
        URI uri = linkTo(OrderController.class).slash("/orderItems" + orderItem.getOrderItemId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}/refundForm")
    public String getRefundForm(@PathVariable Long id, Model model) {
        Orders order = orderService.getOrder(id);
        model.addAttribute("order",order);
        return "/order/refund-form";
    }

    @PostMapping("/{id}/refund")
    public String refundOrder(@PathVariable Long id, Model model) {
//        orderService.updateOrderByRefundOrChangeRequest(id, Orders.OrderStatus.REFUND);
        Orders order = orderService.getOrder(id);
        model.addAttribute("order",order);
        return "/order/refund";
    }

}
