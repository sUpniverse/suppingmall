package com.supshop.suppingmall.order;

import com.supshop.suppingmall.common.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/orders")
@RequiredArgsConstructor
@Controller
@Slf4j
public class OrderController {

    private final OrderService orderService;


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

        if(SessionService.isSessionNull(session)) {
        }
        return "/order/form";
    }

    @PostMapping("")
    public String createOrder(OrderForm orderForm, HttpSession session){
        //성공
        orderService.order(orderForm);
        return "/order/success";

        //실패
    }

    @GetMapping("")
    public String getOrders(@RequestParam(required = false) @DateTimeFormat(pattern = "YYYY-MM-dd") LocalDate fromDate,
                            @RequestParam(required = false) @DateTimeFormat(pattern = "YYYY-MM-dd") LocalDate toDate,
                            @RequestParam(required = false) String type,
                            HttpSession session,
                            Model model) {
        List<Orders> orders = orderService.findOrders(fromDate,toDate);
        model.addAttribute("orders",orders);
        return "/order/list";
    }

    @GetMapping("/{id}")
    public String getOrder(@PathVariable Long id, HttpSession session, Model model) {
        Orders order = orderService.findOrder(id);
        model.addAttribute("order",order);
        return "/order/detail";
    }

    @GetMapping("/main")
    public String getOrderById(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                               @RequestParam(required = false) String type,
                               @RequestParam(required = false) String searchValue,
                               HttpSession session,
                               Model model) {
        Long userId = SessionService.getSessionUser(session).getUserId();
        List<Orders> orders = orderService.findOrderByBuyerId(userId,fromDate,toDate,type,searchValue);
        model.addAttribute("orders",orders);
        return "/order/list";
    }
}
