package com.supshop.suppingmall.cart;

import com.supshop.suppingmall.cart.Form.CartForm;
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
@Transactional
public class CartServiceTest {

    @Autowired CartService cartService;
    @Autowired UserFactory userFactory;
    @Autowired ProductFactory productFactory;
    @Autowired CategoryFactory categoryFactory;
    @Autowired CartFactory cartFactory;

    @Test
    public void saveCart() throws Exception {
        //given
        CartForm cartForm = cartFactory.buildCartForm();

        //when

        Cart cart = cartService.save(cartForm);
        List<Cart> findCarts = cartService.getCartByBuyerId(cart.getBuyer().getUserId());
        Cart findCartByBuyerId = findCarts.get(0);

        //then
        assertThat(findCartByBuyerId.getCartId()).isEqualTo(cart.getCartId());
        assertThat(findCartByBuyerId.getCartItemList()).isNotEmpty();
        assertThat(findCartByBuyerId.getCartItemList().get(0).getProduct().getProductId()).isEqualTo(cart.getCartItemList().get(0).getProduct().getProductId());
        assertThat(findCartByBuyerId.getCartItemList().get(0).getProduct().getSeller().getUserId()).isEqualTo(cart.getCartItemList().get(0).getProduct().getSeller().getUserId());
    }

    @Test
    public void getCart() throws Exception {
        //given
        Cart cart = cartFactory.createCart();

        //when
        Cart savedCart = cartService.getCart(cart.getCartId());

        //then
        assertThat(savedCart).isNotNull();
        assertThat(savedCart.getBuyer().getUserId()).isEqualTo(cart.getBuyer().getUserId());
        assertThat(savedCart.getCartItemList().get(0).getProduct().getProductId()).isEqualTo(cart.getCartItemList().get(0).getProduct().getProductId());

    }

    @Test
    public void deleteCart() throws Exception {
        //given
        Cart cart = cartFactory.createCart();

        //when

        cartService.delete(cart.getCartId());

        Cart savedCart = cartService.getCart(cart.getCartId());

        //then
        assertThat(savedCart).isNull();

    }




}