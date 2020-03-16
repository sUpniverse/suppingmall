package com.supshop.suppingmall.order;

import com.supshop.suppingmall.common.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@RequestMapping("/orders")
@RequiredArgsConstructor
@Controller
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/orderSheet")
    public String getOrderSheet(TempOrderForm tempOrderForm,
                                @ModelAttribute("orderForm") OrderForm orderForm,
                                HttpSession session,
                                Model model) {

        // 임시주문
        Orders tempOrder = orderService.createTempOrder(tempOrderForm);

        // 반영된 주문 표시
        model.addAttribute("orderItems", tempOrder.getOrderItems());
        model.addAttribute("product",tempOrder.getOrderItems().get(0).getProduct());
        model.addAttribute("tempOrder",tempOrder);

        if(SessionService.isSessionNull(session)) {
        }
        return "/order/form";
    }

    @PostMapping("")
    public String createOrder(Orders orders){
        orderService.order(orders);
        return "";
    }
}
