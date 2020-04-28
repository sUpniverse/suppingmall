package com.supshop.suppingmall.cart;

import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductOption;
import lombok.*;

@Getter @Builder @ToString
@NoArgsConstructor @AllArgsConstructor
public class CartItem {

    private Long cartItemId;
    private Cart cart;
    private Product product;
    private ProductOption productOption;
    private int quantity;
    private int price;

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProductOption(ProductOption productOption) {
        this.productOption = productOption;
    }
}
