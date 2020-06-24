package com.sample.oauth.login;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("login")
@Getter
@Setter
public class LoginProperties {

    private boolean corsEnabled;
}
