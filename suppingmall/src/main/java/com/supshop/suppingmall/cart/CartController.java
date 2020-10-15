package com.supshop.suppingmall.cart;

import com.supshop.suppingmall.cart.Form.CartForm;
import com.supshop.suppingmall.common.UserUtils;
import com.supshop.suppingmall.user.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("")
    public String getCart(@AuthenticationPrincipal SessionUser user, Model model) {
        List<Cart> carts = cartService.getCartByBuyerId(user.getUserId());
        model.addAttribute("carts",carts);

        int totalPrice = 0;
        int totalProductPrice = 0;
        int totalDeliveryPrice = 0;

        for(Cart cart : carts) {
            totalProductPrice += cart.getAmountProductPrice();
            totalDeliveryPrice += cart.getDeliveryPrice();
        }

        totalPrice = totalDeliveryPrice + totalProductPrice;

        model.addAttribute("totalPrice",totalPrice);
        model.addAttribute("totalProductPrice",totalProductPrice);
        model.addAttribute("totalDeliveryPrice",totalDeliveryPrice);

        return "/cart/cart";
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity createCart(@RequestBody CartForm cartForm) {
        Cart cart;
        try {
            cart = cartService.save(cartForm);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }

        URI uri = linkTo(CartController.class).slash(cart.getCartId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/{id}")
    @ResponseBody ResponseEntity deleteCart(@PathVariable Long id,
                                            @AuthenticationPrincipal SessionUser sessionUser){

        Cart cart = cartService.getCart(id);
        if(cart == null || !UserUtils.isOwner(cart.getBuyer().getUserId(), sessionUser))
            ResponseEntity.badRequest().body("존재하지 않습니다.");

        cartService.delete(cart.getCartId());
        return ResponseEntity.ok().build();
    }


}
