package com.supshop.suppingmall.api.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(GlobalExceptionHandlerTest.TestController.class)
class GlobalExceptionHandlerTest {

    @LocalServerPort int port;
    @Autowired TestRestTemplate rest;
    private String url(String p){ return "http://localhost:"+port+p; }

    @RestController
    @RequestMapping("/api/t")
    static class TestController {
        @GetMapping("/boom")
        Map<String,Object> boom(){ throw new RuntimeException("boom"); }
    }

    @Test
    void genericError_shouldReturn500_withCode() {
        ResponseEntity<String> res = rest.getForEntity(url("/api/t/boom"), String.class);
        assertThat(res.getStatusCode().value()).isEqualTo(500);
        assertThat(res.getBody()).contains("\"success\":false").contains("\"code\":\"INTERNAL_SERVER_ERROR\"");
    }
}