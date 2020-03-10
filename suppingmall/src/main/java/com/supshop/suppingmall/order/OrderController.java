package com.supshop.suppingmall.order;

import com.supshop.suppingmall.common.SessionService;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductService;
import com.supshop.suppingmall.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/orders")
@Controller
public class OrderController {

    private OrderService orderService;
    private TempOrderService tempOrderService;

    public OrderController(OrderService orderService, TempOrderService tempOrderService) {
        this.orderService = orderService;
        this.tempOrderService = tempOrderService;
    }

    @PostMapping("/orderSheet")
    public String getOrderSheet(TempOrder tempOrder,
                                HttpSession session, Model model) {

        Product product = tempOrderService.setOrderItemsInfo(tempOrder);
        tempOrderService.createTempOrder(tempOrder);

        model.addAttribute("orderItems", tempOrder.getOrderItemList());
        model.addAttribute("product",product);
        model.addAttribute("tempOrder",tempOrder);

        if(SessionService.isSessionNull(session)) {
        }
        return "/order/form";
    }

    @PostMapping("")
    public String createOrder(Orders orders){
        orderService.createOrder(orders);
        return "";
    }
}
