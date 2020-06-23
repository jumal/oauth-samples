## OAuth Login Sample

OAuth login sample using Spring Security 5.1+'s OAuth client support and GitHub.

### Usage

#### Create a GitHub OAuth App

* Go to [https://github.com/settings/developers](https://github.com/settings/developers).
* Click on "New OAuth App" 
* Set "Homepage URL" to `http://localhost:8080`
* Set "Authorization callback URL" to `http://localhost:8080/login/oauth2/code/sample`

#### Add the Client ID and Client Secret

Modify `src/main/resources/application` to set the client ID and secret provided by GitHub:

```properties
spring.security.oauth2.client.registration.sample.client-id=<your-client-id>
spring.security.oauth2.client.registration.sample.client-secret=<your-client-secret>
```

#### Start the Application

* Execute `src/main/java/com/sample/oauth/login/LoginApplication`
* Go to [http://localhost:8080](http://localhost:8080)

### References

[Spring Boot OAuth tutorial](https://spring.io/guides/tutorials/spring-boot-oauth2)

[Spring Security Documentation: Oauth 2.0 Login](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#webflux-oauth2-login)

[Spring Security Documentation: Testing Oauth 2.0 Login](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#webflux-testing-oauth2-login)
