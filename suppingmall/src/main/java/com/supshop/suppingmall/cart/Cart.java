package com.supshop.suppingmall.cart;

import com.supshop.suppingmall.user.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Builder @ToString
@AllArgsConstructor @NoArgsConstructor
public class Cart {

    private Long cartId;
    private User buyer;
    private LocalDateTime createDate;
    private List<CartItem> cartItemList;


    public int getAmountProductPrice() {
        int price = 0;
        for(CartItem cartItem : this.cartItemList) {
            price += cartItem.getPrice();
        }
        return price;
    }

    public int getAmountPrice() {
        int deliveryPrice = getDeliveryPrice();
        return this.getAmountProductPrice() + deliveryPrice;
    }

    public int getDeliveryPrice() {
        return this.cartItemList.get(0).getProduct().getDeliveryPrice();
    }

    public int getAmountProductCount() {
        int count = 0;
        for(CartItem cartItem : this.cartItemList) {
            count += cartItem.getQuantity();
        }
        return count;
    }

}
