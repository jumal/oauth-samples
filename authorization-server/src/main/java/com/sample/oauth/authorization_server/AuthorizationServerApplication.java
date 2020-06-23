package com.sample.oauth.authorization_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AuthorizationServerProperties.class)
public class AuthorizationServerApplication {

    /**
     * @should support the authorization code grant
     * @should support the client credential grant
     * @should support the password grant
     * @should include a jwk set endpoint
     * @should not authenticate the actuator endpoints
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }
}
