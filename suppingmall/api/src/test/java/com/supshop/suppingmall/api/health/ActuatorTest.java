package com.supshop.suppingmall.api.health;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ActuatorTest {

    @LocalServerPort int port;
    @Autowired TestRestTemplate rest;

    @Test
    void health_shouldReturnUp() {
        String url = "http://localhost:" + port + "/actuator/health";
        ResponseEntity<String> res = rest.getForEntity(url, String.class);
        assertThat(res.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(res.getBody()).contains("\"status\":\"UP\"");
    }
}
