package com.supshop.suppingmall.mapper;

import com.supshop.suppingmall.cart.Cart;

import java.util.Optional;

public interface CartMapper {

    Optional<Cart> findByBuyerId(Long id);

    void save(Cart cart);
}
