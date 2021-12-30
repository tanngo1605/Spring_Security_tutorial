package com.example.demo.jwt;

import com.google.common.net.HttpHeaders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConfigurationProperties(prefix = "application.jwt")
public class JWTConfig {

    @Value("${secretKey}")
    private String secretKey;
    @Value("${tokenPrefix}")
    private String tokenPrefix;
    @Value("${tokenExpirationAfterDays}")
    private Integer tokenExpirationAfterDays;

//    public JWTConfig(String secretKey, String tokenPredix, Integer tokenExpirationAfterDays) {
//        this.secretKey = secretKey;
//        this.tokenPredix = tokenPredix;
//        this.tokenExpirationAfterDays = tokenExpirationAfterDays;
//    }
    public JWTConfig(){}

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPredix(String tokenPredix) {
        this.tokenPrefix = tokenPredix;
    }

    public Integer getTokenExpirationAfterDays() {
        return tokenExpirationAfterDays;
    }

    public void setTokenExpirationAfterDays(Integer tokenExpirationAfterDays) {
        this.tokenExpirationAfterDays = tokenExpirationAfterDays;
    }

    public String getAuthorizationHeader(){
        return HttpHeaders.AUTHORIZATION;
    }
}
