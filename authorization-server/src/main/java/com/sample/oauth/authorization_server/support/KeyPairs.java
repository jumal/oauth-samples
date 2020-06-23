package com.sample.oauth.authorization_server.support;

import lombok.SneakyThrows;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Base64.getDecoder;
import static java.util.regex.Pattern.DOTALL;
import static java.util.regex.Pattern.compile;

public class KeyPairs {

    private static final Pattern PEM = compile("-----BEGIN (.*)-----(.*)-----END (.*)-----", DOTALL);

    private KeyPairs() {
        super();
    }

    @SneakyThrows
    public static KeyPair parse(String pem) {
        Matcher matcher = PEM.matcher(pem);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Not PEM encoded");
        }

        byte[] content = matcher.group(2).replaceAll("\\n", "").getBytes(UTF_8);
        RSAPrivateKey key = RSAPrivateKey.getInstance(getDecoder().decode(content));
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = factory.generatePublic(new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent()));
        PrivateKey privateKey = factory.generatePrivate(new RSAPrivateCrtKeySpec(key.getModulus(),
                key.getPublicExponent(), key.getPrivateExponent(), key.getPrime1(), key.getPrime2(), key.getExponent1(),
                key.getExponent2(), key.getCoefficient()));

        return new KeyPair(publicKey, privateKey);
    }
}
