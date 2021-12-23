package br.com.dazo.poc.config;

import br.com.dazo.poc.security.filter.CustomAuthenticationFilter;
import br.com.dazo.poc.security.filter.CustomAuthorizationFilter;
import br.com.dazo.poc.security.service.JWTService;
import br.com.dazo.poc.security.properties.LDAPConfigProperties;
import br.com.dazo.poc.utils.CollectionsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LDAPConfigProperties lDAPConfigProperties;

    @Autowired
    private JWTService jwtService;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .userDnPatterns(lDAPConfigProperties.getUserDn())
                .groupSearchBase(lDAPConfigProperties.getGroup())
                .contextSource(contextSource())
                .passwordCompare()
                .passwordEncoder(new LdapShaPasswordEncoder())
                .passwordAttribute(lDAPConfigProperties.getFieldPassword());
    }

    @Bean
    public DefaultSpringSecurityContextSource contextSource() {
        return new DefaultSpringSecurityContextSource(CollectionsUtils.montaLista(getUrlLdap()), lDAPConfigProperties.getBaseDn());
    }

    private String getUrlLdap() {
        return "ldap://" + lDAPConfigProperties.getUrl() + ":" + lDAPConfigProperties.getPort() + "/";
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().antMatchers("/login", "/refresh/token").permitAll()
                .and()
                .addFilter(new CustomAuthenticationFilter(authenticationManagerBean(), jwtService))
                .addFilterBefore(new CustomAuthorizationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}