package com.bmc.userserivce.security;

import static com.bmc.userserivce.security.ApplicationPermission.BMC_ADMIN;
import static com.bmc.userserivce.security.ApplicationPermission.BMC_USER;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterAfter(new JWTTokenVerifier(),JWTAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/users").hasAuthority(BMC_USER.name())
                .antMatchers(HttpMethod.POST,"/users/").hasAuthority(BMC_USER.name())
                .antMatchers(HttpMethod.GET,"/users/{userId}").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .antMatchers(HttpMethod.POST,"{userId}/document").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .antMatchers(HttpMethod.GET,"{userId}/documents").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .antMatchers(HttpMethod.GET,"{userId}/documents/metadata").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .antMatchers(HttpMethod.GET,"{userId}/documents/{documentName}").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .anyRequest()
                .authenticated();
    }
}
