package com.supshop.suppingmall.payModule;

import org.springframework.hateoas.server.mvc.ControllerLinkRelationProvider;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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
        WebMvcLinkBuilder link = linkTo(ModuleController.class).slash(result.getUid());
        URI uri = link.toUriComponentsBuilder().build().toUri();
        return ResponseEntity.created(uri).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelPay(@PathVariable Long id) {

        return ResponseEntity.ok().build();
    }

}
