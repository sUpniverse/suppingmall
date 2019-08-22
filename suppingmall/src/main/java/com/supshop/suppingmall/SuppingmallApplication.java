package com.supshop.suppingmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = {"com.supshop.suppingmall.mappers"})
public class SuppingmallApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuppingmallApplication.class, args);
    }

}
