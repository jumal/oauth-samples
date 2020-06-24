package com.sample.oauth.login.configuration;

import com.sample.oauth.login.LoginProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import static org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest.toAnyEndpoint;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/error", "/webjars/**").permitAll()
                .matchers(toAnyEndpoint()).permitAll()
                .anyExchange().authenticated().and()
                .oauth2Login().and()
                .cors().and()
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(LoginProperties properties) {
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        if (properties.isCorsEnabled()) {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.applyPermitDefaultValues();
            configurationSource.registerCorsConfiguration("/**", configuration);
        }
        return configurationSource;
    }
}
