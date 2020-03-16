package com.supshop.suppingmall.payModule;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payModule")
public class ModuleController {

    @PostMapping("")
    public ResponseEntity<String> getModule(@RequestBody PayInitInfo payInitInfo){

        int start = (int)(Math.random()*27);
        long uid = Long.parseLong(UUID.randomUUID().toString().replace("-", "").substring(start, start + 6));
        long apply = Long.parseLong(UUID.randomUUID().toString().replace("-", "").substring(start, start + 16));
        Result.builder()
                .uid(uid)
                .merchant_uid(payInitInfo.getMerchant_uid())
                .paid_amount(payInitInfo.getAmount())
                .apply_num(apply)
                .build();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelPay(@PathVariable Long id) {

        return ResponseEntity.ok().build();
    }

}
