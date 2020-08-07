package com.supshop.suppingmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan(value = {"com.supshop.suppingmall.mapper"})
@EnableAsync
public class SuppingmallApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuppingmallApplication.class, args);
    }

}
