package com.supshop.suppingmall.order;

import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.order.Form.OrderForm;
import com.supshop.suppingmall.order.Form.TempOrderForm;
import com.supshop.suppingmall.page.Criteria;
import com.supshop.suppingmall.page.OrderCriteria;
import com.supshop.suppingmall.page.PageMaker;
import com.supshop.suppingmall.user.Role;
import com.supshop.suppingmall.user.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/orders")
@RequiredArgsConstructor
@Controller
@Slf4j
public class OrderController {

    private final OrderService orderService;

    private static final int orderDisplayPagingNum = 5;

    @PostMapping("/orderSheet")
    public String getOrderSheet(TempOrderForm tempOrderForm,
                                @ModelAttribute(value = "orderForm") OrderForm orderForm,
                                HttpSession session,
                                Model model) {

        // 임시주문
        Orders tempOrder = orderService.createOrder(tempOrderForm);

        // 반영된 주문 표시
        model.addAttribute("orderItems", tempOrder.getOrderItems());
        model.addAttribute("product",tempOrder.getOrderItems().get(0).getProduct());
        model.addAttribute("tempOrder",tempOrder);
        return "/order/form";
    }

    @PostMapping("")
    public String createOrder(OrderForm orderForm, HttpSession session){
        System.out.println(orderForm.toString());
        orderService.order(orderForm);
        //성공
        return "/order/success";


    }

    @GetMapping("")
    public String getOrders(@RequestParam(required = false) @DateTimeFormat(pattern = "YYYY-MM-dd") LocalDate fromDate,
                            @RequestParam(required = false) @DateTimeFormat(pattern = "YYYY-MM-dd") LocalDate toDate,
                            @RequestParam(required = false) String type,
                            Model model) {
        List<Orders> orders = orderService.findOrders(fromDate,toDate);
        model.addAttribute("orders",orders);
        return "/order/list";
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable Long id,
                           @AuthenticationPrincipal SessionUser user,
                           Model model) {
        Orders order = orderService.findOrder(id);
        model.addAttribute("order",order);
        if(user.getRole().equals(Role.SELLER)) {
            return "/order/seller/detail";
        } else if(user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.MASTER) ) {
            return "/order/seller/detail";
        }
        return "/order/detail";
    }

    @GetMapping("/main")
    public String getOrdersByBuyerId(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                    @RequestParam(required = false) String type,
                                    @RequestParam(required = false) Orders.OrderStatus status,
                                    @AuthenticationPrincipal SessionUser user,
                                    Model model) {

//        if(sessionUser.getRole().equals(User.Role.SELLER)) {
//            List<Orders> sellerOrders = orderService.findOrderBySellerId(sessionUser.getUserId(),fromDate,toDate,type,status);
//            model.addAttribute("orders",sellerOrders);
//            model.addAttribute("statusList", Arrays.asList(Delivery.DeliveryStatus.values()));
//            return "/order/seller/list";
//        } else if(sessionUser.getRole().equals(User.Role.ADMIN) || sessionUser.getRole().equals(User.Role.MASTER) ) {
//            return "/order/seller/list";
//        }
        List<Orders> orders = orderService.findOrderByBuyerId(user.getUserId(),fromDate,toDate,type,status);
        model.addAttribute("orders",orders);
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
                                     OrderCriteria criteria,
                                     Model model) {


        List<Orders> orders = orderService.findOrderBySellerId(user.getUserId(),fromDate,toDate,type,deliveryStatus,orderStatus,null);
        PageMaker pageMaker = new PageMaker(orders.size(), orderDisplayPagingNum, criteria);
        List<Orders> pagingOrders = orderService.findOrderBySellerId(user.getUserId(),fromDate,toDate,type,deliveryStatus,orderStatus,criteria);

        model.addAttribute("orders",pagingOrders);
        model.addAttribute("pageMaker",pageMaker);

        if(type != null && type.equals("order")) {
            model.addAttribute("statusList", Arrays.asList(Orders.OrderStatus.CANCEL,Orders.OrderStatus.REFUND,Orders.OrderStatus.CHANGE));
            return "/order/seller/refund-list";
        }

        model.addAttribute("statusList", Arrays.asList(Delivery.DeliveryStatus.values()));
        return "/order/seller/list";
    }

    @GetMapping("/{id}/seller")
    public String getOrderBySellerId(@PathVariable Long id, Model model) {
        Orders order = orderService.findOrder(id);
        model.addAttribute("order",order);
        return "/order/seller/detail";
    }

    @GetMapping("/{id}/cancelForm")
    public String getCancelForm(@PathVariable Long id, Model model) {
        Orders order = orderService.findOrder(id);
        model.addAttribute("order",order);
        return "/order/cancel-form";
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, Model model) {
        orderService.cancelOrder(id);
        Orders order = orderService.findOrder(id);
        model.addAttribute("order",order);
        return "/order/cancel";
    }

    @GetMapping("/{id}/refundForm")
    public String getRefundForm(@PathVariable Long id, Model model) {
        Orders order = orderService.findOrder(id);
        model.addAttribute("order",order);
        return "/order/refund-form";
    }

    @PostMapping("/{id}/refund")
    public String refundOrder(@PathVariable Long id, Model model) {
        orderService.updateOrderByRefundOrChangeRequest(id, Orders.OrderStatus.REFUND);
        Orders order = orderService.findOrder(id);
        model.addAttribute("order",order);
        return "/order/refund";
    }

}
