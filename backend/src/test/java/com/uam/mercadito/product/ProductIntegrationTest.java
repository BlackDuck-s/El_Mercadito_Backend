package com.uam.mercadito.product;

import com.uam.mercadito.product.dto.ProductCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProductIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    void publicProductsList_returns200() {
        ResponseEntity<String> resp = restTemplate.getForEntity(
            baseUrl() + "/products?page=0&size=10",
            String.class
        );

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertTrue(resp.getBody().contains("content")); // por el Page<>
    }

    @Test
    void createProduct_withoutToken_isUnauthorizedOrForbidden() {
        var dto = new ProductCreateDTO(
            "Test Product",
            "Test description",
            new BigDecimal("10.00"),
            5,
            1L
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProductCreateDTO> entity = new HttpEntity<>(dto, headers);

        ResponseEntity<String> resp = restTemplate.postForEntity(
            baseUrl() + "/products",
            entity,
            String.class
        );

        // Dependiendo de la configuración, puede ser 401 o 403;
        // aceptamos ambos como "no autorizado".
        assertTrue(
            resp.getStatusCode() == HttpStatus.UNAUTHORIZED
            || resp.getStatusCode() == HttpStatus.FORBIDDEN
        );
    }
}