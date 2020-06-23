## OAuth Resource Server Sample

OAuth resource server sample using the new Spring Security 5.1+'s OAuth resource server support.

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

#### Use the JWT Token to Access a Protected Resource
 
```
curl -v -H "Authorization: Bearer ${TOKEN}" http://localhost:8082/test
```

### Reference

[Spring Boot OAuth tutorial](https://spring.io/guides/tutorials/spring-boot-oauth2)
