package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.cart.CartItem;

import java.util.List;

public interface CartItemMapper {

    void save(List<CartItem> cartItemList);

}
