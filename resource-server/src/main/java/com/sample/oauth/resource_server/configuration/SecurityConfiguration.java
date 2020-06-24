package com.sample.oauth.resource_server.configuration;

import com.sample.oauth.resource_server.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.Collection;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest.toAnyEndpoint;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .matchers(toAnyEndpoint()).permitAll()
                .anyExchange().authenticated().and()
                .oauth2ResourceServer(server -> server.jwt(jwt -> jwt.jwtAuthenticationConverter(authorities())))
                .cors().and()
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(ResourceServerProperties properties) {
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        if (properties.isCorsEnabled()) {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.applyPermitDefaultValues();
            configurationSource.registerCorsConfiguration("/**", configuration);
        }
        return configurationSource;
    }

    private static Converter<Jwt, Mono<AbstractAuthenticationToken>> authorities() {
        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtAuthoritiesExtractor());
        return new ReactiveJwtAuthenticationConverterAdapter(authenticationConverter);
    }

    public static class JwtAuthoritiesExtractor implements Converter<Jwt, Collection<GrantedAuthority>> {

        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            return ((Collection<?>) jwt.getClaims().getOrDefault("authorities", emptyList()))
                    .stream()
                    .map(Object::toString)
                    .map(SimpleGrantedAuthority::new)
                    .collect(toList());
        }
    }
}
