package br.com.dazo.poc.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JWTConfigProperties {

    private Integer timeAccess;

    private Integer timeRefresh;

    private String secret;

    private String accessToken = "access_token";

    private String refreshToken = "refresh_token";

}
