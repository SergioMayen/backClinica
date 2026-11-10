package com.example.auth.config;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
public class JwtConfig {
    @Bean RSAPrivateKey jwtPrivateKey(ResourceLoader loader, @Value("${security.jwt.private-key}") String location) throws Exception {
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(readPem(loader, location, "PRIVATE KEY")));
    }
    @Bean RSAPublicKey jwtPublicKey(ResourceLoader loader, @Value("${security.jwt.public-key}") String location) throws Exception {
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(readPem(loader, location, "PUBLIC KEY")));
    }
    @Bean JwtEncoder jwtEncoder(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        RSAKey jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        return new NimbusJwtEncoder(new ImmutableJWKSet<SecurityContext>(new com.nimbusds.jose.jwk.JWKSet(jwk)));
    }
    @Bean JwtDecoder jwtDecoder(RSAPublicKey publicKey) { return NimbusJwtDecoder.withPublicKey(publicKey).build(); }
    private byte[] readPem(ResourceLoader loader, String location, String label) throws Exception {
        Resource resource = loader.getResource(location);
        try (InputStream in = resource.getInputStream()) {
            String pem = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            String encoded = pem.replace("-----BEGIN " + label + "-----", "").replace("-----END " + label + "-----", "").replaceAll("\\s", "");
            return Base64.getDecoder().decode(encoded);
        }
    }
}
