package com.supshop.suppingmall.api.common;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = { "management.endpoints.web.exposure.include=health,info" }
)
@Import(ResponseWrappingAdviceTest.TestController.class)
class ResponseWrappingAdviceTest {

    @LocalServerPort int port;
    @Autowired TestRestTemplate rest;
    String url(String p){ return "http://localhost:"+port+p; }

    @RestController
    @RequestMapping("/api/t")
    static class TestController {
        record Req(@NotBlank String name) {}

        @GetMapping("/echo")
        Map<String,Object> echo(){ return Map.of("echo","ok"); }

        @PostMapping("/validate")
        Map<String,Object> v(@Valid @RequestBody Req r){ return Map.of("name", r.name()); }

        @GetMapping("/boom")
        Map<String,Object> boom(){ throw new RuntimeException("boom"); }
    }

    @Test
    void normalApi_shouldBeWrapped() {
        ResponseEntity<String> res = rest.getForEntity(url("/api/t/echo"), String.class);
        assertThat(res.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(res.getBody())
                .contains("\"success\":true")
                .contains("\"data\"")
                .contains("\"traceId\"");
    }

    @Test
    void validationError_shouldReturn400_withStandardError() {
        HttpHeaders h = new HttpHeaders(); h.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> res = rest.postForEntity(
                url("/api/t/validate"), new HttpEntity<>("{\"name\":\"\"}", h), String.class);

        assertThat(res.getStatusCode().value()).isEqualTo(400);
        assertThat(res.getBody())
                .contains("\"success\":false")
                .contains("\"code\""); // VALIDATION_.. 코드 문자열 포함 여부는 구현에 맞게 조정
    }

    @Test
    void genericError_shouldReturn500_withStandardError() {
        ResponseEntity<String> res = rest.getForEntity(url("/api/t/boom"), String.class);
        assertThat(res.getStatusCode().value()).isEqualTo(500);
        assertThat(res.getBody())
                .contains("\"success\":false")
                .contains("\"code\":\"INTERNAL_SERVER_ERROR\"");
    }
}