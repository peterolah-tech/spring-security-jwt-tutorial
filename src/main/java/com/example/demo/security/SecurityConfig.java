package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/signin").permitAll() // allowed by anyone
                .antMatchers(HttpMethod.GET, "/vehicles/**").authenticated() // allowed only when signed in
                .antMatchers(HttpMethod.DELETE, "/vehicles/**").hasRole("ADMIN") // allowed if signed in with ADMIN role
                .anyRequest().denyAll(); // anything else is denied
    }

    // For logging users in we need first to expose Spring Security's AuthenticationManager to other classes, so it can the autowired if needed.
    // Put this code to SecurityConfig.java:
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
