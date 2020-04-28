package com.supshop.suppingmall.cart;

import com.supshop.suppingmall.product.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartItemFactory {

    public List<CartItem> createItemList(Product product) {
        CartItem cartItem = CartItem.builder()
                .quantity(2)
                .price(10000)
                .product(product)
                .productOption(product.getOptions().get(0))
                .build();

        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);

        return cartItemList;
    }
}
