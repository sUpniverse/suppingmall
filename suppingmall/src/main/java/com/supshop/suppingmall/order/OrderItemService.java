package com.supshop.suppingmall.order;

import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.delivery.DeliveryService;
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
    private final DeliveryService deliveryService;

    private static final int hour = 23;
    private static final int minute = 59;

    public int getOrderItemCount(String type, String searchValue,  LocalDate fromDate, LocalDate toDate) {
        LocalDateTime formDateTime = getFormDateTime(fromDate);
        LocalDateTime toDateTime = getToDateTime(toDate);
        String code = getCode(type, searchValue);
        return orderItemMapper.findCount(type,code,formDateTime,toDateTime);
    }


    public int getOrderItemCountByBuyerId(Long id,String type, String searchValue, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime formDateTime = getFormDateTime(fromDate);
        LocalDateTime toDateTime = getToDateTime(toDate);
        String code = getCode(type, searchValue);
        return orderItemMapper.findCountByBuyerId(id,type,code,formDateTime,toDateTime);
    }
    public int getOrderItemCountBySellerId(Long id,String type, String searchValue, LocalDate fromDate, LocalDate toDate) {
        LocalDateTime formDateTime = getFormDateTime(fromDate);
        LocalDateTime toDateTime = getToDateTime(toDate);
        String code = getCode(type, searchValue);
        return orderItemMapper.findCountBySellerId(id,type,code,formDateTime,toDateTime);
    }

    public OrderItem getOrderItem(Long orderItemId) {
        return orderItemMapper.findOne(orderItemId);
    }

    /**
     * 주문상품 bulk insert
     * @param orderItems
     */
    @Transactional
    public void saveList(List<OrderItem> orderItems) {
        orderItemMapper.saveList(orderItems);
    }

    /**
     * 주문 상품 bulk update
     * @param orderItems
     */
    @Transactional
    public void updateOrderItemList(List<OrderItem> orderItems) {
        orderItemMapper.updateList(orderItems);
    }

    @Transactional
    public void updateOrderItem(OrderItem orderItem) {
        orderItemMapper.update(orderItem);
    }

    /**
     * 구매자의 관점에서 주문을 조회
     * @param userId
     * @param fromDate
     * @param toDate
     * @param type
     * @param orderStatus
     * @return List<OrderItem>, 구매자가 구매한 주문 목록
     */
    public List<OrderItem> getOrderItemByBuyerId(Long userId, LocalDate fromDate, LocalDate toDate, String type, Orders.OrderStatus orderStatus, TenItemsCriteria criteria) {
        LocalDateTime formDateTime = getFormDateTime(fromDate);
        LocalDateTime toDateTime = getToDateTime(toDate);
        //code : orderStatus code
        String code = Optional.ofNullable(orderStatus).map(Orders.OrderStatus::getCode).orElse(null);
        List<OrderItem> orderItemList = orderItemMapper.findByBuyerId(userId, formDateTime, toDateTime, type,  code, criteria);
        return orderItemList;
    }

    /**
     * 판매자의 관점에서 주문을 조회
     * @param userId
     * @param fromDate
     * @param toDate
     * @param type
     * @param searchValue
     * @param criteria
     * @return List<OrderItem>, 판매자에게 요청된 주문 목록
     */
    public List<OrderItem> getOrderItemBySellerId(Long userId, LocalDate fromDate, LocalDate toDate, String type, String searchValue, TenItemsCriteria criteria) {
        LocalDateTime formDateTime = getFormDateTime(fromDate);
        LocalDateTime toDateTime = getToDateTime(toDate);

        //code : orderStatus Value
        String code = getCode(type, searchValue);

        List<OrderItem> orderItemList = orderItemMapper.findBySellerId(userId, formDateTime, toDateTime, type,  code, criteria);
        return orderItemList;
    }


    /**
     * 주문 취소 (제품 보내기 전 결제 취소 시)
     * @param orderItem
     */
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

    /**
     * OrderItem의 상태변경 로직, orderStatus에 따른 로직이 분기 됌
     * @param orderItem
     * @param orderStatus
     */
    @Transactional
    public void changeStatus(OrderItem orderItem, Orders.OrderStatus orderStatus) {
        Delivery delivery = orderItem.getDelivery();

        //Todo : 반품, 교환에 따른 택배사에 요청 로직
        if(Orders.OrderStatus.DELIVERY.equals(orderStatus)) {         // 배송준비
            delivery.setStatus(Delivery.DeliveryStatus.WAIT);
        } else if(Orders.OrderStatus.REFUND.equals(orderStatus)) {   // 환불 요청시 택배사에 반품요청
            delivery.setStatus(Delivery.DeliveryStatus.REFUND);
        } else if(Orders.OrderStatus.CHANGE.equals(orderStatus)) {  // 교환 요청시 택배사에 교환요청
            delivery.setStatus(Delivery.DeliveryStatus.CHANGE);
        } else if(Orders.OrderStatus.COMPLETE.equals(orderStatus)) {
            delivery.setStatus(Delivery.DeliveryStatus.COMPLETE);
        }

        deliveryService.update(delivery);
        orderItem.setStatus(orderStatus);
        orderItemMapper.updateStatus(orderItem);
    }


    /*
    //상품교환 확정 시
    @Transactional
    public Long updateOrderAfterCheckingChange(Long orderId) {
        // 주문 가져오기
        Orders order = orderMapper.findOne(orderId).get();

        // 상품 개수에 반환된 물품개수 추가
        List<OrderItem> orderItems = order.getOrderItems();
        List<ProductOption> productOptionList = new ArrayList<>();
        for(OrderItem orderItem : orderItems) {
            ProductOption productOption = orderItem.getProductOption();
            productOption.addStock(orderItem.getCount());
            productOptionList.add(productOption);
        }
        productService.updateProductOption(productOptionList);

        // 다시 보내야할 상품 개수를 통해 물품개수 감소


        return orderId;
    }
    */

    /**
     * type이 delivery일 경우 deliveryStatus를 이용함
     * type이 order 경우 orderStatus를 이용함
     * @param type
     * @param searchValue
     * @return
     */
    public String getCode(String type, String searchValue) {
        String code = "";
        if(type != null && "delivery".equals(type)) {
            code = Delivery.DeliveryStatus.getCodeByEnumString(searchValue);
        } else if(type != null && "order".equals(type)) {
            code = Orders.OrderStatus.getCodeByEnumString(searchValue);
        }
        return code;
    }

    /**
     * 기준일 00:00분
     * @param fromDate
     * @return
     */
    private LocalDateTime getFormDateTime(LocalDate fromDate) {
        return Optional.ofNullable(fromDate).map(LocalDate::atStartOfDay).orElse(null);
    }

    /**
     * 기준일 23:59분
     * @param toDate
     * @return
     */
    private LocalDateTime getToDateTime(LocalDate toDate) {
        return Optional.ofNullable(toDate).map(localDate -> toDate.atTime(hour, minute)).orElse(null);
    }


}
