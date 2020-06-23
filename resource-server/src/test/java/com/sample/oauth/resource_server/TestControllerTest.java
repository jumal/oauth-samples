package com.sample.oauth.resource_server;

import com.sample.oauth.resource_server.configuration.SecurityConfiguration.JwtAuthoritiesConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static java.util.Collections.singletonList;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
public class TestControllerTest {

    @Autowired
    private WebTestClient webClient;

    /**
     * @verifies require authentication
     * @see TestController#test(Jwt)
     */
    @Test
    public void test_should_require_authentication() {
        webClient.get()
                .uri("/test")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    /**
     * @verifies require authorization
     * @see TestController#test(Jwt)
     */
    @Test
    public void test_should_require_authorization() {
        webClient.mutateWith(mockJwt())
                .get()
                .uri("/test")
                .exchange()
                .expectStatus().isForbidden();
    }

    /**
     * @verifies return the resource
     * @see TestController#test(Jwt)
     */
    @Test
    public void test_should_return_the_resource() {
        webClient.mutateWith(mockJwt()
                .jwt(jwt -> jwt.claim("authorities", singletonList("ROLE_USER")))
                .authorities(new JwtAuthoritiesConverter()))
                .get()
                .uri("/test")
                .exchange()
                .expectStatus().isOk();
    }
}
