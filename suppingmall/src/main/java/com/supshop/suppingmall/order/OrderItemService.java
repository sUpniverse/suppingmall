package com.supshop.suppingmall.order;

import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.exception.AlreadyCanceledException;
import com.supshop.suppingmall.mapper.OrderItemMapper;
import com.supshop.suppingmall.page.TenItemsCriteria;
import com.supshop.suppingmall.payModule.ModuleController;
import com.supshop.suppingmall.payment.Payment;
import com.supshop.suppingmall.payment.PaymentService;
import com.supshop.suppingmall.product.ProductOption;
import com.supshop.suppingmall.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.AlreadyBoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemMapper orderItemMapper;
    private final PaymentService paymentService;
    private final ModuleController moduleController;
    private final ProductService productService;

    private static final int hour = 23;
    private static final int minute = 59;

    public OrderItem getOrderItem(Long orderItemId) {

        return orderItemMapper.findOne(orderItemId);
    }

    @Transactional
    public void saveList(List<OrderItem> orderItems) {
        orderItemMapper.saveList(orderItems);
    }

    @Transactional
    public void updateOrderItemList(List<OrderItem> orderItems) {
        orderItemMapper.updateList(orderItems);
    }

    //구매자의 관점에서 주문을 조회
    public List<OrderItem> getOrderByBuyerId(Long userId, LocalDate fromDate, LocalDate toDate, String type, Orders.OrderStatus status) {
        LocalDateTime formDateTime = Optional.ofNullable(fromDate).map(LocalDate::atStartOfDay).orElse(null);
        LocalDateTime toDateTime = Optional.ofNullable(toDate).map(localDate -> toDate.atTime(hour, minute)).orElse(null);
        //code : orderStatus Value
        String code = Optional.ofNullable(status).map(Orders.OrderStatus::getCode).orElse(null);
        List<OrderItem> orderItemList = orderItemMapper.findByBuyerId(userId, formDateTime, toDateTime, type,  code);
        return orderItemList;
    }

    //판매자의 관점에서 주문을 조회
    public List<OrderItem> getOrderBySellerId(Long userId, LocalDate fromDate, LocalDate toDate, String type, Delivery.DeliveryStatus deliveryStatus, Orders.OrderStatus orderStatus, TenItemsCriteria criteria) {
        LocalDateTime formDateTime = Optional.ofNullable(fromDate).map(LocalDate::atStartOfDay).orElse(null);
        LocalDateTime toDateTime = Optional.ofNullable(toDate).map(localDate -> toDate.atTime(hour, minute)).orElse(null);

        //code : orderStatus Value
        String code = "";
        if(type != null && "delivery".equals(type)) {
            code = Optional.ofNullable(deliveryStatus).map(Delivery.DeliveryStatus::getCode).orElse(null);
        } else if(type != null && "order".equals(type)) {
            code = Optional.ofNullable(orderStatus).map(Orders.OrderStatus::getCode).orElse(null);
        }
        List<OrderItem> orderItemList = orderItemMapper.findBySellerId(userId, formDateTime, toDateTime, type,  code, criteria);
        return orderItemList;
    }

    //주문 취소 (제품 보내기 전 결제 취소 시)
    @Transactional
    public void cancelOrderItem(OrderItem orderItem) {

        //결제 정보 가져오기
        Payment payment = orderItem.getPayment();

        //이미 결제 취소했는지
        if(orderItem.getStatus().equals(Orders.OrderStatus.CANCEL) && payment.getStatus().equals(Payment.PaymentStatus.CANCEL))
            throw new AlreadyCanceledException("이미 취소된 요청입니다.");

        String vendorCheckNumber = payment.getVendorCheckNumber();
        int price = payment.getPrice();

        //실제 모듈 적용시
//        HttpHeaders headers = new HttpHeaders();
//        HttpEntity entity = new HttpEntity(headers);
//        URI payModuleURI = UriComponentsBuilder.fromHttpUrl(payModuleUrl + "/" + vendorCheckNumber).build().toUri();
//        ResponseEntity<String> response = restTemplate.exchange(payModuleURI, HttpMethod.DELETE, entity, String.class);

        // 테스트용 모듈
        ResponseEntity<String> response = moduleController.cancelPay(vendorCheckNumber,price);
        if(!response.getStatusCode().is2xxSuccessful()) {
            //재 전송 RetryTemplate 같은걸 사용 예정
            //Todo : 시간초과, 금액 초과(취소 불 가능 금액) 등등 에러처리
            throw new RuntimeException("결제모듈 오류");
        }

        payment.setStatus(Payment.PaymentStatus.CANCEL);
        paymentService.cancelPayment(payment);

        // 주문 상태 변경
        orderItem.setStatus(Orders.OrderStatus.CANCEL);

        // 물품 수량 변경
        List<ProductOption> productOptionList = new ArrayList<>();
        ProductOption productOption = orderItem.getProductOption();
        productOption.addStock(orderItem.getCount());
        productOptionList.add(productOption);

        productService.updateProductOption(productOptionList);

        //주문상태 수정
        orderItemMapper.updateStatus(orderItem);
    }
}
