package com.sample.oauth.authorization_server.configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey.Builder;
import com.sample.oauth.authorization_server.AuthorizationServerProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.interfaces.RSAPublicKey;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static com.sample.oauth.authorization_server.support.KeyPairs.parse;
import static org.springframework.security.core.authority.AuthorityUtils.authorityListToSet;

@Configuration
@EnableAuthorizationServer
@AllArgsConstructor
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final AuthorizationServerProperties properties;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder clientsBuilder = clients.inMemory();
        properties.getClients().forEach((key, client) -> clientsBuilder
                .withClient(client.getClientId())
                .secret(passwordEncoder.encode(client.getClientSecret()))
                .scopes(asArray(client.getScope()))
                .authorizedGrantTypes(asArray(client.getAuthorizedGrantTypes()))
                .redirectUris(asArray(client.getRegisteredRedirectUri()))
        );
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationConfiguration.getAuthenticationManager())
                .accessTokenConverter(accessTokenConverter())
                .tokenStore(tokenStore());
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtConverter = new JwtAccessTokenConverter();
        jwtConverter.setKeyPair(parse(properties.getJwtKey()));

        DefaultAccessTokenConverter userAttributesConverter = new DefaultAccessTokenConverter();
        userAttributesConverter.setUserTokenConverter(new DefaultUserAuthenticationConverter() {
            @Override
            public Map<String, ?> convertUserAuthentication(Authentication authentication) {
                Map<String, Object> response = new LinkedHashMap<>();
                response.put("iss", properties.getJwtIssuer());
                response.put("sub", authentication.getName());
                if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
                    response.put(AUTHORITIES, authorityListToSet(authentication.getAuthorities()));
                }
                return response;
            }
        });
        jwtConverter.setAccessTokenConverter(userAttributesConverter);

        return jwtConverter;
    }

    private static String[] asArray(Set<String> set) {
        return set != null ? set.toArray(new String[0]) : new String[0];
    }

    @FrameworkEndpoint
    static class JwkSetEndpoint {

        private final Map<String, Object> key;

        JwkSetEndpoint(AuthorizationServerProperties properties) {
            RSAPublicKey publicKey = (RSAPublicKey) parse(properties.getJwtKey()).getPublic();
            key = new JWKSet(new Builder(publicKey).build()).toJSONObject();
        }

        @GetMapping("/.well-known/jwks.json")
        @ResponseBody
        public Map<String, Object> getKey() {
            return key;
        }
    }
}
