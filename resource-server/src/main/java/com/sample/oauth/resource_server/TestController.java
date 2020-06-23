package com.sample.oauth.resource_server;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.just;

@RestController
public class TestController {

    @GetMapping("/")
    public Mono<Jwt> notAuthorized(@AuthenticationPrincipal Jwt principal) {
        return just(principal);
    }

    @GetMapping("/authorized")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Mono<Jwt> authorized(@AuthenticationPrincipal Jwt principal) {
        return just(principal);
    }
}
