package com.bmc.appointmentserivce.security;

import static com.bmc.appointmentserivce.security.ApplicationPermission.BMC_ADMIN;
import static com.bmc.appointmentserivce.security.ApplicationPermission.BMC_USER;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


/**
 * This class has the configuration and security rules added to all end points supported by this availability services
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
                .antMatchers(HttpMethod.POST,"/doctors/{doctorId}/availability").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .antMatchers(HttpMethod.GET,"/doctors/{doctorId}/availability").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .antMatchers(HttpMethod.POST,"/appointments").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .antMatchers(HttpMethod.GET,"/appointments/{appointmentId}").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .antMatchers(HttpMethod.PUT,"/appointments/{appointmentId}").hasAuthority(BMC_USER.name())
                .antMatchers(HttpMethod.GET,"/users/{userId}/appointments").hasAnyAuthority(BMC_USER.name(),BMC_ADMIN.name())
                .antMatchers(HttpMethod.POST,"/prescriptions").hasAuthority(BMC_USER.name())
                .antMatchers(HttpMethod.POST,"/prescriptions/").hasAuthority(BMC_USER.name())
                .anyRequest()
                .authenticated();
    }
}
