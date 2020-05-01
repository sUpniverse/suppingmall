package com.supshop.suppingmall.cart.Form;


import com.supshop.suppingmall.cart.CartItem;
import lombok.*;

import java.util.List;

@Getter @Builder
@ToString
@AllArgsConstructor @NoArgsConstructor
public class CartForm {

    private Long productId;
    private Long buyerId;
    private List<CartItem> cartItemList;
    
    public void setOrderItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }
}
