package com.supshop.suppingmall.api.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(RequestLoggingFilterTest.TestController.class)
class RequestLoggingFilterTest {
    @LocalServerPort int port;
    @Autowired TestRestTemplate rest;
    private String url(String p){ return "http://localhost:"+port+p; }

    @RestController
    @RequestMapping("/api/t")
    static class TestController {
        @GetMapping("/echo")
        Map<String,Object> echo(){ return Map.of("echo","ok"); }
    }

    @Test
    void shouldLogEachRequest(CapturedOutput output) {
        rest.getForEntity(url("/api/t/echo"), String.class);
        assertThat(output.getOut())
                .contains("GET /api/t/echo")
                .contains("-> 200");
    }
}