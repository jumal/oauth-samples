## OAuth Resource Server Sample

OAuth resource server sample using Spring Security 5.1+'s OAuth resource server support.

### Usage

#### Start the Authorization Server

Execute **authorization-server**/`src/main/java/com/sample/oauth/authorization_server/AuthorizationServerApplication`

#### Get a JWT token

For simplicity, using a password flow:

```
TOKEN=$(curl -s client-3-id:client-3-secret@localhost:8081/oauth/token -dgrant_type=password -dscope=any -dusername=enduser -dpassword=password | jq -r .access_token)
```

#### Start the Resource Server

Execute **resource-server**/`src/main/java/com/sample/oauth/resource_server/ResourceServerApplication`

#### Use the JWT Token to Access an Authenticated Resource
 
```
curl -i -H "Authorization: Bearer ${TOKEN}" http://localhost:8082
```

#### Use the JWT Token to Access an Authorised Resource
 
```
curl -i -H "Authorization: Bearer ${TOKEN}" http://localhost:8082/authorized
```

### References

[Spring Security Documentation: Oauth 2.0 Resource Server](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#webflux-oauth2-resource-server)

[Spring Security Documentation: Testing JWT Authentication](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#webflux-testing-jwt)
