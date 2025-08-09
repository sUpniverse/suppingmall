package com.supshop.suppingmall.api.docs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpenApiTest {
    @LocalServerPort int port;
    @Autowired TestRestTemplate rest;

    @Test
    void openapi_shouldServeJson() {
        String url = "http://localhost:" + port + "/api/v1/openapi";
        ResponseEntity<String> res = rest.getForEntity(url, String.class);
        assertThat(res.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(res.getBody()).contains("\"openapi\"");
    }
}