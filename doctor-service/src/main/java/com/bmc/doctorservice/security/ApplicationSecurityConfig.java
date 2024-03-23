package com.bmc.doctorservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import static com.bmc.doctorservice.security.ApplicationPermission.BMC_USER;
import static com.bmc.doctorservice.security.ApplicationPermission.BMC_ADMIN;

/**
 * This class has the configuration and security rules added to all end points supported by this doctor services
 *
 */
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
                .antMatchers(HttpMethod.POST,"/doctors").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .antMatchers(HttpMethod.GET,"/doctors/{doctorId}").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .antMatchers(HttpMethod.GET,"/doctors").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .antMatchers(HttpMethod.PUT,"/doctors/{doctorId}/approve").hasAuthority(BMC_ADMIN.name())
                .antMatchers(HttpMethod.PUT,"/doctors/{doctorId}/approve/").hasAuthority(BMC_ADMIN.name())
                .antMatchers(HttpMethod.PUT,"/doctors/{doctorId}/reject").hasAuthority(BMC_ADMIN.name())
                .antMatchers(HttpMethod.PUT,"/doctors/{doctorId}/reject/").hasAuthority(BMC_ADMIN.name())
                .antMatchers(HttpMethod.POST,"{doctorId}/document").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .antMatchers(HttpMethod.POST,"{doctorId}/documents").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .antMatchers(HttpMethod.GET,"{doctorId}/documents/metadata").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .antMatchers(HttpMethod.GET,"{doctorId}/documents/{documentName}").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .anyRequest()
                .authenticated();
    }
}
