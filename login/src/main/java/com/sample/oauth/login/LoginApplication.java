package com.sample.oauth.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import static org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest.toAnyEndpoint;

@SpringBootApplication
@EnableReactiveMethodSecurity
public class LoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginApplication.class, args);
    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(authorizeExchange -> authorizeExchange
                        .pathMatchers("/error", "/webjars/**").permitAll()
                        .matchers(toAnyEndpoint()).permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2Login()
                .and().csrf().disable()
                .cors()
                .and().build();
    }

    @Bean
    @ConditionalOnProperty(name = "corsEnabled")
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.applyPermitDefaultValues();
        configurationSource.registerCorsConfiguration("/**", configuration);
        return configurationSource;
    }
}
