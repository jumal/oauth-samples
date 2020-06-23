package com.sample.oauth.authorization_server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.web.server.LocalManagementPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class AuthorizationServerApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorizationServerProperties properties;

    @LocalManagementPort
    private int managementPort;

    /**
     * @verifies support the authorization code grant
     * @see AuthorizationServerApplication#main(String[])
     */
    @Test
    public void main_should_support_the_authorization_code_grant() throws Exception {
        String url = "/oauth/authorize?grant_type=authorization_code&response_type=code&client_id={clientId}&state=1234";
        BaseClientDetails client = properties.getClients().get("authorisation-code-test");
        mockMvc.perform(get(url, client.getClientId()))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("WWW-Authenticate", "Basic realm=\"Realm\""));
    }

    /**
     * @verifies support the client credential grant
     * @see AuthorizationServerApplication#main(String[])
     */
    @Test
    public void main_should_support_the_client_credential_grant() throws Exception {
        String url = "/oauth/token";
        BaseClientDetails client = properties.getClients().get("client-credential-test");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");
        params.add("scope", "any");
        mockMvc.perform(post(url)
                .params(params)
                .with(httpBasic(client.getClientId(), client.getClientSecret())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.access_token").exists());
    }

    /**
     * @verifies support the password grant
     * @see AuthorizationServerApplication#main(String[])
     */
    @Test
    public void main_should_support_the_password_grant() throws Exception {
        String url = "/oauth/token";
        BaseClientDetails client = properties.getClients().get("password-test");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("scope", "any");
        params.add("username", "enduser");
        params.add("password", "password");
        mockMvc.perform(post(url)
                .params(params)
                .with(httpBasic(client.getClientId(), client.getClientSecret())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.access_token").exists());
    }

    /**
     * @verifies include a jwk set endpoint
     * @see AuthorizationServerApplication#main(String[])
     */
    @Test
    public void main_should_include_a_jwk_set_endpoint() throws Exception {
        mockMvc.perform(get("/.well-known/jwks.json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.keys").isArray())
                .andExpect(jsonPath("$.keys.[0].kty").value("RSA"));
    }

    /**
     * @verifies not authenticate the actuator endpoints
     * @see AuthorizationServerApplication#main(String[])
     */
    @Test
    public void main_should_not_authenticate_the_actuator_endpoints() {
        String url = "http://localhost:" + managementPort + "/actuator/health";
        ResponseEntity<String> response = new RestTemplate().getForEntity(url, String.class);
        assertEquals(OK, response.getStatusCode());
    }
}
