package com.supshop.suppingmall.delivery;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping("/{id}/form")
    public String getDeliveryForm(@PathVariable Long id, Model model) {
        Delivery delivery = deliveryService.getDelivery(id);
        model.addAttribute("delivery",delivery);
        model.addAttribute("vendors", Delivery.DeliveryVendor.values());
        return "/delivery/form";
    }

    @PostMapping("/{id}/vendor")
    @ResponseBody
    public ResponseEntity saveVendor(@PathVariable Long id,@RequestBody Delivery delivery) {
        deliveryService.saveVendor(id,delivery);
        return ResponseEntity.ok().build();
    }

}
