package com.supshop.suppingmall.user;

import org.springframework.security.test.context.support.WithUserDetails;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@WithUserDetails(userDetailsServiceBeanName = "userDetailsService")
public @interface WithSessionUser {

    String value() default "user";

}
