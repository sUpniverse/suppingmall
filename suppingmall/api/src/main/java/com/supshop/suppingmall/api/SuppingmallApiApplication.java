package com.supshop.suppingmall.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.supshop.suppingmall")
public class SuppingmallApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SuppingmallApiApplication.class, args);
    }
}
