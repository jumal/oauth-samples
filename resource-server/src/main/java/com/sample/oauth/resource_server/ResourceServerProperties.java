package com.sample.oauth.resource_server;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("resource-server")
@Getter
@Setter
public class ResourceServerProperties {

    private boolean corsEnabled;
}
