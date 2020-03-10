package com.supshop.suppingmall.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/vendorService")
public class CardVendorController {

    @GetMapping("")
    public String getPayService(PayInfo payInfo) {
        return "/payment";
    }

    @PostMapping("")
    @ResponseBody
    public ResponseEntity<Payment> createPayment(PayInfo payInfo) {
        return ResponseEntity.ok(Payment.builder().build());
    }
}
