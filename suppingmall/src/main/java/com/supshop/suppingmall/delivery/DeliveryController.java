package com.supshop.suppingmall.delivery;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/{id}/form")
    public String getDeliveryForm(@PathVariable Long id, HttpSession session, Model model) {
        Delivery delivery = deliveryService.findDelivery(id);
        model.addAttribute("delivery",delivery);
        return "/delivery/form";
    }

    @PostMapping("/{id}/vendor")
    @ResponseBody
    public ResponseEntity saveVendor(@PathVariable Long id,@RequestBody Delivery delivery, HttpSession session) {
        deliveryService.saveVendor(id,delivery);
        return ResponseEntity.ok().build();
    }

}
