package com.sample.oauth.resource_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ResourceServerProperties.class)
public class ResourceServerApplication {

    /**
     * @should require bearer token authentication
     * @should support JWT based authentication
     * @should support JWT based authorization
     * @should not authenticate the actuator endpoints
     */
    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }
}
