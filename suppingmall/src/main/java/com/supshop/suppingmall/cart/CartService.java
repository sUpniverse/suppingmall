package com.supshop.suppingmall.cart;

import com.supshop.suppingmall.cart.Form.CartForm;
import com.supshop.suppingmall.mapper.CartItemMapper;
import com.supshop.suppingmall.mapper.CartMapper;
import com.supshop.suppingmall.product.Product;
import com.supshop.suppingmall.product.ProductService;
import com.supshop.suppingmall.user.User;
import com.supshop.suppingmall.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {

    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    private final ProductService productService;
    private final UserService userService;

    public List<Cart> findCartByBuyerId(Long id) {
        return cartMapper.findByBuyerId(id);
    }

    @Transactional
    public Cart save(CartForm cartForm) {

        Cart cart = this.buildCartByForm(cartForm);
        cartMapper.save(cart);

        for (CartItem cartItem : cartForm.getCartItemList()) {
            cartItem.setCart(cart);
        }
        cartItemMapper.save(cartForm.getCartItemList());
        return cart;
    }

    private Cart buildCartByForm(CartForm cartForm) {
        User user = userService.getUser(cartForm.getBuyerId());
        Product product = productService.findProduct(cartForm.getProductId());

        List<CartItem> cartItems = cartForm.getCartItemList();

        // Form을 이용해 작성된 한정된 정보(단지 productId, productOptionId)의 상품정보를 이용해 가져온 정보를 보충해줌
        for(CartItem cartItem : cartItems) {
            cartItem.setProduct(product);
            cartItem.setProductOption(product.getOptions().get(cartItem.getProductOption().getOptionId()-1));
        }

        Cart cart = Cart.builder()
                .buyer(user)
                .cartItemList(cartItems)
                .build();

        return cart;
    }
}
