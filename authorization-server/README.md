## OAuth Authorization Server Sample

OAuth authorization server sample using the legacy Spring Security OAuth.

### Usage

#### Start the Authorization Server

Execute `src/main/java/com/sample/oauth/authorization_server/AuthorizationServerApplication`

#### Test a Client Credential Flow

```
curl -v client-2-id:client-2-secret@localhost:8081/oauth/token -dgrant_type=client_credentials -dscope=any
```

#### Test an Authorization Code Flow

Browse to [http://localhost:8081/oauth/authorize?grant_type=authorization_code&response_type=code&client_id=client-1-id&state=1234](http://localhost:8081/oauth/authorize?grant_type=authorization_code&response_type=code&client_id=client-1-id&state=1234)

#### Test a Password Flow

```
curl -v client-3-id:client-3-secret@localhost:8081/oauth/token -dgrant_type=password -dscope=any -dusername=enduser -dpassword=password
```

### References

[Spring Security OAuth2 Boot documentation](https://docs.spring.io/spring-security-oauth2-boot/docs/current/reference/html5/)
[Spring Security OAuth2 Authorization Server Sample](https://github.com/spring-projects/spring-security/tree/5.3.3.RELEASE/samples/boot/oauth2authorizationserver)