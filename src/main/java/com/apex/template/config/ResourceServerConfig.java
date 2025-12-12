package com.apex.template.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final TokenStore tokenStore;

    public ResourceServerConfig(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/oauth/token").permitAll()
                .antMatchers("/all/**").permitAll()
                .antMatchers("/api/admin/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                .antMatchers("/api/manager/**").hasAnyRole("MANAGER")
                .antMatchers("/api/**").authenticated();
    }
}