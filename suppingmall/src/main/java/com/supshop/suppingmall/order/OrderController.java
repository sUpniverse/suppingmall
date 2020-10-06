package com.supshop.suppingmall.order;

import com.supshop.suppingmall.common.UserUtils;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.order.form.OrderForm;
import com.supshop.suppingmall.order.form.TempOrderForm;
import com.supshop.suppingmall.page.TenItemsCriteria;
import com.supshop.suppingmall.page.PageMaker;
import com.supshop.suppingmall.product.Product;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping("/orders")
@Controller
@RequiredArgsConstructor @Slf4j
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;

    private static final int orderDisplayPagingNum = 5;
    private static final String redirectUrl = "redirect:/orders/main";


    @PostMapping("/orderSheet")
    @ResponseBody
    public ResponseEntity createOrderSheet(@RequestBody List<TempOrderForm> tempOrderFormList,
                                           @ModelAttribute(value = "orderForm") OrderForm orderForm) {

        // 임시주문
        Orders tempOrder = orderService.createTempOrder(tempOrderFormList);
        URI link = linkTo(OrderController.class).slash(tempOrder.getOrderId()).slash("/orderSheet").toUri();

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

    @GetMapping("/{id}/orderSheet")
    public String getOrderSheet(@PathVariable Long id,
                                @AuthenticationPrincipal SessionUser user,
                                Model model) {

        Orders order = orderService.getOrder(id);
        if(!UserUtils.isOwner(order.getOrderItems().get(0).getBuyer().getUserId(), user)) {
            return "redirect:/products/";
        }

        Map<Long,List<OrderItem>> map = new HashMap<>();

        for (OrderItem orderItem : order.getOrderItems()) {
            Long productId = orderItem.getProduct().getProductId();
            orderItem.getProduct().setSeller(orderItem.getSeller());
            if (map.containsKey(productId)) {
                map.get(productId).add(orderItem);
                continue;
            }
            ArrayList<OrderItem> list = new ArrayList<>();
            list.add(orderItem);
            map.put(productId, list);
        }

        Set<Product> productList = order.getOrderItems().stream().map(OrderItem::getProduct).collect(Collectors.toSet());
        int totalDeliveryPrice = productList.stream().mapToInt(Product::getDeliveryPrice).sum();


        // 반영된 주문 표시
        model.addAttribute("itemList", map);
        model.addAttribute("productList",productList);
        model.addAttribute("tempOrder",order);
        model.addAttribute("totalDeliveryPrice",totalDeliveryPrice);
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
    public String getOrderItemByBuyerId(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                        @RequestParam(required = false) String type,
                                        @RequestParam(required = false) Orders.OrderStatus status,
                                        @AuthenticationPrincipal SessionUser user,
                                        Model model) {

        List<OrderItem> orderItemList = orderItemService.getOrderItemByBuyerId(user.getUserId(), fromDate, toDate, type, status);
        model.addAttribute("orderItemList",orderItemList);
        model.addAttribute("statusList", Arrays.asList(Orders.OrderStatus.values()));

        return "/order/list";
    }


    @GetMapping("/seller/main")
    public String getOrderItemBySellerIdOnDelivery(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                   @RequestParam(required = false) String type,
                                                   @RequestParam(required = false) Delivery.DeliveryStatus deliveryStatus,
                                                   @RequestParam(required = false) Orders.OrderStatus orderStatus,
                                                   @AuthenticationPrincipal SessionUser user,
                                                   TenItemsCriteria criteria,
                                                   Model model) {


        int count = orderItemService.getOrderItemCount("seller", user.getUserId());
        PageMaker pageMaker = new PageMaker(count, orderDisplayPagingNum, criteria);
        List<OrderItem> orderItemList = orderItemService.getOrderItemBySellerId(user.getUserId(), fromDate, toDate, type, deliveryStatus, orderStatus, criteria);

        model.addAttribute("orderItemList",orderItemList);
        model.addAttribute("pageMaker",pageMaker);

        // type이 order일시에는 환불관련 페이지로
        if(type != null && type.equals("order")) {
            model.addAttribute("statusList", Arrays.asList(Orders.OrderStatus.CANCEL,Orders.OrderStatus.REFUND,Orders.OrderStatus.CHANGE));
            return "/order/seller/refund-list";
        }

        int check = (int) orderItemList.stream().filter(orderItem -> orderItem.getStatus().equals(Orders.OrderStatus.ORDER)).count();
        int wait = (int) orderItemList.stream().filter(orderItem -> orderItem.getStatus().equals(Orders.OrderStatus.DELIVERY) && orderItem.getDelivery().getStatus().equals(Delivery.DeliveryStatus.WAIT)).count();
        int delivery = (int) orderItemList.stream().filter(orderItem -> orderItem.getStatus().equals(Orders.OrderStatus.DELIVERY) && orderItem.getDelivery().getStatus().equals(Delivery.DeliveryStatus.DELIVERY)).count();
        int complete = (int) orderItemList.stream().filter(orderItem -> orderItem.getStatus().equals(Orders.OrderStatus.DELIVERY) && orderItem.getDelivery().getStatus().equals(Delivery.DeliveryStatus.COMPLETE)).count();
        int change = (int) orderItemList.stream().filter(orderItem -> orderItem.getStatus().equals(Orders.OrderStatus.CHANGE) && orderItem.getDelivery().getStatus().equals(Delivery.DeliveryStatus.CHANGE)).count();
        int refund = (int) orderItemList.stream().filter(orderItem -> orderItem.getStatus().equals(Orders.OrderStatus.REFUND) && orderItem.getDelivery().getStatus().equals(Delivery.DeliveryStatus.CHANGE)).count();


        Map<String,Integer> status = new HashMap<>();
        status.put("check", check);
        status.put("wait", wait);
        status.put("delivery", delivery);
        status.put("complete",complete);
        status.put("change", change+refund);

        model.addAttribute("status", status);
        model.addAttribute("statusList", Delivery.DeliveryStatus.values());

        return "/order/seller/list";
    }


    @GetMapping("/{id}/seller")
    public String getOrderBySellerId(@PathVariable Long id,
                                     @AuthenticationPrincipal SessionUser sessionUser,
                                     Model model) {
        Orders order = orderService.getOrder(id);
        if(order == null && !UserUtils.isOwner(order.getOrderItems().get(0).getSeller().getUserId(), sessionUser)){
            new IllegalArgumentException();
            return redirectUrl;
        }

        List<OrderItem> orderItemListBySeller = order.getOrderItems().stream().filter(orderItem -> orderItem.getSeller().getUserId().equals(sessionUser.getUserId())).collect(Collectors.toList());
        model.addAttribute("orderItemList",orderItemListBySeller);
        return "/order/seller/detail";
    }

    @PostMapping("/{id}/seller/status/check")
    @ResponseBody
    public ResponseEntity getOrderCheckBySeller(@PathVariable Long id,
                                                @AuthenticationPrincipal SessionUser sessionUser) {

        OrderItem orderItem = orderItemService.getOrderItem(id);

        try {
            if(!UserUtils.isOwner(orderItem.getSeller().getUserId(), sessionUser)) new IllegalArgumentException("해당 셀러가 아닙니다.");
            if(orderItem == null) new IllegalArgumentException("해당 주문이 존재하지 않습니다.");
            orderItemService.changeStatus(orderItem, Orders.OrderStatus.DELIVERY);
        } catch (Exception e){
            ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }


        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cancel")
    @ResponseBody
    public ResponseEntity cancelOrder(@PathVariable Long id,
                              @AuthenticationPrincipal SessionUser sessionUser) {
        OrderItem orderItem = orderItemService.getOrderItem(id);

        if(isAlreadyOnStatus(orderItem, Orders.OrderStatus.CANCEL,sessionUser)){
            new IllegalArgumentException();
            return ResponseEntity.badRequest().body("존재하지 않습니다.");
        }

        orderItem.getProductOption().setProductId(orderItem.getProduct().getProductId());
        orderItemService.cancelOrderItem(orderItem);
        URI uri = linkTo(OrderController.class).slash("/orderItems" + orderItem.getOrderItemId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}/refundForm")
    public String getRefundForm(@PathVariable Long id,
                                @AuthenticationPrincipal SessionUser sessionUser,
                                Model model) {

        OrderItem orderItem = orderItemService.getOrderItem(id);
        if(isAlreadyOnStatus(orderItem, Orders.OrderStatus.REFUND,sessionUser)){
            new IllegalArgumentException();
            return redirectUrl;
        }
        model.addAttribute("orderItem",orderItem);
        return "/order/refund-form";
    }

    @PostMapping("/{id}/refund")
    public String refundOrder(@PathVariable Long id,
                              @AuthenticationPrincipal SessionUser sessionUser,
                              Model model) {
        OrderItem orderItem = orderItemService.getOrderItem(id);
        if(isAlreadyOnStatus(orderItem, Orders.OrderStatus.REFUND,sessionUser)){
            new IllegalArgumentException();
            return redirectUrl;
        }
        orderItemService.changeStatus(orderItem, Orders.OrderStatus.REFUND);
        model.addAttribute("orderItem",orderItem);
        return "/order/refund";
    }


    @PostMapping("/{id}/complete")
    public String completeOrder(@PathVariable Long id,
                              @AuthenticationPrincipal SessionUser sessionUser,
                              Model model) {
        OrderItem orderItem = orderItemService.getOrderItem(id);
        if(isAlreadyOnStatus(orderItem, Orders.OrderStatus.COMPLETE,sessionUser)){
            new IllegalArgumentException();
            return redirectUrl;
        }
        orderItemService.changeStatus(orderItem, Orders.OrderStatus.COMPLETE);
        model.addAttribute("orderItem",orderItem);
        return "/order/refund";
    }

    private boolean isAlreadyOnStatus(OrderItem orderItem, Orders.OrderStatus status,SessionUser user){
        if(orderItem == null || status.equals(orderItem.getStatus())
                || !UserUtils.isOwner(orderItem.getBuyer().getUserId(),user)) {
            return true;
        }
        return false;
    }


}
