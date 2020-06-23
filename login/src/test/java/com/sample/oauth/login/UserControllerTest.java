package com.sample.oauth.login;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockOAuth2Login;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
public class UserControllerTest {

    @Autowired
    private WebTestClient webClient;

    /**
     * @verifies require authentication
     * @see UserController#user(OAuth2User)
     */
    @Test
    public void user_should_require_authentication() {
        webClient
                .get()
                .uri("/user")
                .exchange()
                .expectStatus().isFound()
                .expectHeader().valueEquals("Location", "/oauth2/authorization/sample")
                .expectBody().isEmpty();
    }

    /**
     * @verifies return the user
     * @see UserController#user(OAuth2User)
     */
    @Test
    public void user_should_return_the_user() {
        String name = "Name";
        webClient
                .mutateWith(mockOAuth2Login()
                        .attributes(attributes -> attributes.put("name", name))
                )
                .get()
                .uri("/user")
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.name").isEqualTo(name);
    }
}
