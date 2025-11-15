package com.uam.mercadito.auth;

import com.uam.mercadito.role.Role;
import com.uam.mercadito.role.RoleRepository;
import com.uam.mercadito.auth.AuthDTOs.LoginRequest;
import com.uam.mercadito.auth.AuthDTOs.RegisterRequest;
import com.uam.mercadito.auth.AuthDTOs.TokenResponse;
import com.uam.mercadito.auth.AuthDTOs.MeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AuthIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    RoleRepository roleRepository;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    @BeforeEach
    void ensureClientRole() {
        roleRepository.findByName("CLIENT")
            .orElseGet(() -> roleRepository.save(Role.builder().name("CLIENT").build()));
    }

    @Test
    void registerThenLoginAndMe_flowOk() {
        var regReq = new RegisterRequest("test.client@uam.mx", "123456");

        ResponseEntity<TokenResponse> regResp = restTemplate.postForEntity(
            baseUrl() + "/auth/register",
            regReq,
            TokenResponse.class
        );

        assertEquals(HttpStatus.CREATED, regResp.getStatusCode());
        assertNotNull(regResp.getBody());
        assertNotNull(regResp.getBody().accessToken());

        var loginReq = new LoginRequest("test.client@uam.mx", "123456");

        ResponseEntity<TokenResponse> loginResp = restTemplate.postForEntity(
            baseUrl() + "/auth/login",
            loginReq,
            TokenResponse.class
        );

        assertEquals(HttpStatus.OK, loginResp.getStatusCode());
        assertNotNull(loginResp.getBody());
        assertNotNull(loginResp.getBody().accessToken());

        String token = loginResp.getBody().accessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<MeResponse> meResp = restTemplate.exchange(
            baseUrl() + "/auth/me",
            HttpMethod.GET,
            entity,
            MeResponse.class
        );

        assertEquals(HttpStatus.OK, meResp.getStatusCode());
        assertNotNull(meResp.getBody());
        assertEquals("test.client@uam.mx", meResp.getBody().user());
        assertTrue(meResp.getBody().roles().contains("ROLE_CLIENT"));
    }
}
