package com.supshop.suppingmall.payModule;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/payModule")
public class ModuleController {

    @PostMapping("")
    public ResponseEntity getModule(@RequestBody PayInitInfo payInitInfo){

        String uid = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        String applyNumber = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Result result = Result.builder()
                .uid(uid)
                .merchant_uid(payInitInfo.getMerchant_uid())
                .paid_amount(payInitInfo.getAmount())
                .apply_num(applyNumber)
                .pay_method(payInitInfo.getPay_method())
                .build();
        URI uri = linkTo(ModuleController.class).slash(result.getUid()).toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @DeleteMapping("/{id}/{price}")
    public ResponseEntity<String> cancelPay(@PathVariable String id,@PathVariable int price) {

        return ResponseEntity.ok().build();
    }

}
