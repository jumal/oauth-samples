package com.sample.oauth.authorization_server;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties("authorization-server")
@Getter
@Setter
public class AuthorizationServerProperties {

    private String jwtIssuer;
    private String jwtKey;
    private final Map<String, BaseClientDetails> clients = new HashMap<>();
}
