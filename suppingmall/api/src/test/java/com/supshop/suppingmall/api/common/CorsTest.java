package com.supshop.suppingmall.api.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CorsTest {
    @LocalServerPort int port;
    @Autowired TestRestTemplate rest;

    @Test
    void preflight_shouldReturnOK() {
        HttpHeaders h = new HttpHeaders();
        h.setOrigin("http://localhost:3000");
        h.setAccessControlRequestMethod(HttpMethod.GET);
        ResponseEntity<String> res = rest.exchange(
                "http://localhost:"+port+"/api/v1/health",
                HttpMethod.OPTIONS, new HttpEntity<>(h), String.class);
        assertThat(res.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(res.getHeaders().getAccessControlAllowOrigin()).isEqualTo("http://localhost:3000");
    }
}
