package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.config.TestSecurityConfiguration;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.context.SecurityContextServerWebExchangeWebFilter;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.mycompany.myapp.web.rest.TestUtil.ID_TOKEN;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.*;

/**
 * Integration tests for the {@link AccountResource} REST controller.
 */
@SpringBootTest(classes = {JhipsterApp.class, TestSecurityConfiguration.class})
public class AccountResourceIT {

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private UserService userService;

    private OidcIdToken idToken;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AccountResource accountResource = new AccountResource(userService);

        this.webTestClient = WebTestClient.bindToController(accountResource)
            .apply(springSecurity())
            .controllerAdvice(exceptionTranslator)
            .webFilter(new SecurityContextServerWebExchangeWebFilter())
            .build();

        Map<String, Object> claims = new HashMap<>();
        claims.put("groups", Collections.singletonList(AuthoritiesConstants.ADMIN));
        claims.put("sub", "jane");
        claims.put("email", "jane.doe@jhipster.com");
        this.idToken = new OidcIdToken(ID_TOKEN, Instant.now(),
            Instant.now().plusSeconds(60), claims);
    }

    @Test
    public void testNonAuthenticatedUser() {
        webTestClient.get().uri("/api/authenticate")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody().isEmpty();
    }

    @Test
    public void testAuthenticatedUser() {
        Jwt jwt = Jwt.withTokenValue("token")
            .header("alg", "none")
            .claim("preferred_username", "user").build();

        webTestClient.mutateWith(mockJwt(jwt))
            .mutateWith(csrf())
            .get().uri("/api/authenticate")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class).isEqualTo("user");
    }

    @Test
    public void testGetExistingAccount() {
        webTestClient
            .mutateWith(mockAuthentication(TestUtil.authenticationToken(idToken)))
            .mutateWith(csrf())
            .get().uri("/api/account")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
            .expectBody()
            .jsonPath("$.login").isEqualTo("jane")
            .jsonPath("$.email").isEqualTo("jane.doe@jhipster.com")
            .jsonPath("$.authorities").isEqualTo(AuthoritiesConstants.ADMIN);
    }

    @Test
    public void testGetUnknownAccount() {
        webTestClient.get().uri("/api/account")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is5xxServerError();
    }
}
