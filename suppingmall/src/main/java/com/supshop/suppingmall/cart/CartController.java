package com.supshop.suppingmall.cart;

import com.supshop.suppingmall.cart.Form.CartForm;
import com.supshop.suppingmall.common.UserUtils;
import com.supshop.suppingmall.user.UserVO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final ModelMapper modelMapper;

    @GetMapping("")
    public String getCart(HttpSession session, Model model) {
        if(UserUtils.isSessionNull(session)) {
            return "redirect:/users/loginform";
        }
        UserVO sessionUser = UserUtils.getSessionUser(session);
        List<Cart> carts = cartService.findCartByBuyerId(sessionUser.getUserId());
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
        System.out.println(cartForm.toString());


        Cart cart = cartService.save(cartForm);
        WebMvcLinkBuilder link = linkTo(CartController.class).slash(1l);
        URI uri = link.toUriComponentsBuilder().build().toUri();
        return ResponseEntity.created(uri).build();
    }

}
