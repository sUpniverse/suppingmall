package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.cart.Cart;

import java.util.List;


public interface CartMapper {

    Cart findOne(Long id);

    List<Cart> findAllByBuyerId(Long id);

    void save(Cart cart);

    void delete(Long cartId);
}
