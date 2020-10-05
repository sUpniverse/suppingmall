package com.supshop.suppingmall.cart;

import com.supshop.suppingmall.cart.Form.CartForm;
import com.supshop.suppingmall.category.CategoryFactory;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductFactory;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CartFactory {

    @Autowired CartService cartService;
    @Autowired UserFactory userFactory;
    @Autowired ProductFactory productFactory;
    @Autowired CategoryFactory categoryFactory;

    public List<CartItem> buildItemList(Product product) {
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

    public CartForm buildCartForm(){
        User buyer = userFactory.createUser("buyer");
        User seller = userFactory.createSeller("seller");
        Product product = productFactory.createProduct("macbookPro",seller);
        List<CartItem> itemList = buildItemList(product);

        CartForm cartForm = CartForm.builder()
                .cartItemList(itemList)
                .buyerId(buyer.getUserId())
                .productId(product.getProductId())
                .build();

        return cartForm;
    }

    public Cart createCart(){
        CartForm cartForm = buildCartForm();
        Cart cart = cartService.save(cartForm);
        return cart;
    }
}
