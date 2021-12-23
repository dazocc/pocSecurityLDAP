package br.com.dazo.poc.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.ldap.embedded")
@Getter
@Setter
public class LDAPConfigProperties {

    private String url;

    private Integer port;

    private String ldif;

    private String baseDn;

    private String userDn;

    private String fieldPassword;

    private String group;

}
