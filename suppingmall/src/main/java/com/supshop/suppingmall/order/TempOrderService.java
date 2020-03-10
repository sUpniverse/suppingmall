package com.supshop.suppingmall.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supshop.suppingmall.mapper.TempOrderMapper;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class TempOrderService {

    private ProductService productService;
    private TempOrderMapper tempOrderMapper;

    public TempOrderService(ProductService productService, TempOrderMapper tempOrderMapper) {
        this.productService = productService;
        this.tempOrderMapper = tempOrderMapper;
    }

    private List<OrderItem> getOrderItemJson(String orderItems) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderItem> items = null;
        try {
            items = Arrays.asList(objectMapper.readValue(orderItems, OrderItem[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    public Product setOrderItemsInfo(TempOrder tempOrder){
        Product product = productService.retrieveProduct(tempOrder.getProductId());
        List<OrderItem> orderItems = this.getOrderItemJson(tempOrder.getOrderItems());
        for (OrderItem orderItem : orderItems) {
            int optionId = orderItem.getProductOption().getOptionId();
            orderItem.setProductOption(product.getOptions().get(optionId - 1));
        }
        tempOrder.setOrderItemList(orderItems);
        return product;
    }

    public Long createTempOrder(TempOrder tempOrder) {
        tempOrder.setAmountCount();
        tempOrder.setAmountPrice();

        return 1l;
    }
}
