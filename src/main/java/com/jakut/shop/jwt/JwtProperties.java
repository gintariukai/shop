package com.jakut.shop.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;


@Data
@Validated
@Component
@ConfigurationProperties(prefix = "api.jwt")
public class JwtProperties {

    private long validTimeInSeconds = 1800;

    private String secretKey;

    public long getValidTimeInMillis() {
        return validTimeInSeconds * 1000;
    }

    public byte[] getSecretKeyAsBytes() {
        return secretKey.getBytes();
    }
}
