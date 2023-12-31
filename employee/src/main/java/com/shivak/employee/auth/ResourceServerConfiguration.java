package com.shivak.employee.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * @author Ahasan Habib
 * @since 03 06 20
 */


@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "oauth2-resource";
    private static final String SECURED_READ_SCOPE = "#oauth2.hasScope('READ')";
    private static final String SECURED_WRITE_SCOPE = "#oauth2.hasScope('WRITE')";
    private static final String SECURED_PATTERN = "/**";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .sessionManagement().disable()
                .authorizeRequests()
                .antMatchers("/employee/list").permitAll()
                .antMatchers(HttpMethod.POST, "/user/add").permitAll()
                .antMatchers(HttpMethod.GET, "/user/verify/{id}").permitAll()
                .antMatchers(HttpMethod.POST, "/user/forgetPassword").permitAll()
                .antMatchers(HttpMethod.POST, "/user/updatePassword").permitAll()
                .and()
                .formLogin()
                .and()
                .requestMatchers().antMatchers(SECURED_PATTERN)
                .and()
                .authorizeRequests().antMatchers(HttpMethod.POST, SECURED_PATTERN)
                .access(SECURED_WRITE_SCOPE).anyRequest()
                .access(SECURED_READ_SCOPE);
    }
}
