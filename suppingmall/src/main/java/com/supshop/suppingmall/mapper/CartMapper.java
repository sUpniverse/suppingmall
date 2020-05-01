package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.cart.Cart;

import java.util.List;


public interface CartMapper {

    List<Cart> findByBuyerId(Long id);

    void save(Cart cart);
}
