package com.supshop.suppingmall.cart;

import com.supshop.suppingmall.cart.Form.CartForm;
import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.category.CategoryFactory;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductFactory;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CartServiceTest {

    @Autowired CartService cartService;
    @Autowired UserFactory userFactory;
    @Autowired ProductFactory productFactory;
    @Autowired CategoryFactory categoryFactory;
    @Autowired CartItemFactory cartItemFactory;

    @Test
    @Transactional
    public void saveCart() throws Exception {
        //given
        User buyer = userFactory.createUser("buyer");
        User seller = userFactory.createUser("seller");
        Category exam = categoryFactory.createCategory("exam");
        Product product = productFactory.createProduct("macbookPro",seller,exam);
        List<CartItem> itemList = cartItemFactory.createItemList(product);

        //when
        CartForm cartForm = CartForm.builder()
                .cartItemList(itemList)
                .buyerId(buyer.getUserId())
                .productId(product.getProductId())
                .build();

        Cart cart = cartService.save(cartForm);
        List<Cart> findCarts = cartService.findCartByBuyerId(cart.getBuyer().getUserId());
        Cart findCartByBuyerId = findCarts.get(0);

        //then
        assertThat(findCartByBuyerId.getCartId()).isEqualTo(cart.getCartId());
        assertThat(findCartByBuyerId.getCartItemList()).isNotEmpty();
        assertThat(findCartByBuyerId.getCartItemList().get(0).getProduct().getProductId()).isEqualTo(itemList.get(0).getProduct().getProductId());
        assertThat(findCartByBuyerId.getCartItemList().get(0).getProduct().getSeller().getUserId()).isEqualTo(seller.getUserId());
    }


}