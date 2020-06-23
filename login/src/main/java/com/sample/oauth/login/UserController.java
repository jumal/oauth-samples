package com.sample.oauth.login;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

import static java.util.Collections.singletonMap;
import static reactor.core.publisher.Mono.just;

@RestController
public class UserController {

    /**
     * @should require authentication
     * @should return the user
     */
    @GetMapping("/user")
    public Mono<Map<String, Object>> user(@AuthenticationPrincipal OAuth2User principal) {
        return just(singletonMap("name", principal.getAttribute("name")));
    }
}
