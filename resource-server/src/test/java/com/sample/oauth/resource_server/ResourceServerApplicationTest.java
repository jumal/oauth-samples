package com.sample.oauth.resource_server;

import com.sample.oauth.resource_server.configuration.SecurityConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static java.util.Collections.singletonList;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;
import static org.springframework.test.web.reactive.server.WebTestClient.bindToServer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
public class ResourceServerApplicationTest {

    @Autowired
    private WebTestClient webClient;

    @LocalManagementPort
    private int managementPort;

    /**
     * @verifies require bearer token authentication
     * @see ResourceServerApplication#main(String[])
     */
    @Test
    public void main_should_require_bearer_token_authentication() {
        webClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectHeader().valueEquals("WWW-Authenticate", "Bearer");
    }

    /**
     * @verifies support JWT based authentication
     * @see ResourceServerApplication#main(String[])
     */
    @Test
    public void main_should_support_JWT_based_authentication() {
        webClient.mutateWith(mockJwt())
                .get()
                .uri("/")
                .exchange()
                .expectStatus().isOk();
    }

    /**
     * @verifies support JWT based authorization
     * @see ResourceServerApplication#main(String[])
     */
    @Test
    public void main_should_support_JWT_based_authorization() {
        webClient.mutateWith(mockJwt())
                .get()
                .uri("/authorized")
                .exchange()
                .expectStatus().isForbidden();
        webClient.mutateWith(mockJwt()
                .jwt(jwt -> jwt.claim("authorities", singletonList("ROLE_USER")))
                .authorities(new SecurityConfiguration.JwtAuthoritiesConverter()))
                .get()
                .uri("/authorized")
                .exchange()
                .expectStatus().isOk();
    }

    /**
     * @verifies not authenticate the actuator endpoints
     * @see ResourceServerApplication#main(String[])
     */
    @Test
    public void main_should_not_authenticate_the_actuator_endpoints() {
        bindToServer().baseUrl("http://localhost:" + managementPort).build()
                .get()
                .uri("/actuator/health")
                .exchange()
                .expectStatus().isOk();
    }
}
