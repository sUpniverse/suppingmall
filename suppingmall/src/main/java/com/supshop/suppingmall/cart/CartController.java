package com.supshop.suppingmall.cart;

import com.supshop.suppingmall.cart.Form.CartForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping("/basket")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ModelMapper modelMapper;

    @GetMapping("/users/{userId}")
    public String getBasket(@PathVariable Long userId) {
        cartService.findCartByBuyerId(userId);
        return "";
    }

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity createCart(CartForm cartForm) {

        Cart cart = cartService.save(cartForm);
        WebMvcLinkBuilder link = linkTo(CartController.class).slash(cart.getCartId());
        URI uri = link.toUriComponentsBuilder().build().toUri();
        return ResponseEntity.created(uri).build();
    }

}
