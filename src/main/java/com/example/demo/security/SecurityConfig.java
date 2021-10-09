package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // As you can see in getTokenFromRequest tokens should be sent in the HTTP header in a key-value pair that looks like this:
    // Authorization: Bearer ...
    //
    //Finally add the filter to our SecurityConfig, this will cause it to be activated:

    private final JwtTokenServices jwtTokenServices;

    public SecurityConfig(JwtTokenServices jwtTokenServices) {
        this.jwtTokenServices = jwtTokenServices;
    }

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
                .antMatchers(HttpMethod.GET, "/me").authenticated() // allowed only when signed in
                .antMatchers(HttpMethod.GET, "/username").authenticated() // allowed only when signed in
                .antMatchers(HttpMethod.DELETE, "/vehicles/**").hasRole("ADMIN") // allowed if signed in with ADMIN role
                .anyRequest().denyAll() // anything else is denied
                .and()
                .addFilterBefore(new JwtTokenFilter(jwtTokenServices), UsernamePasswordAuthenticationFilter.class);
    }

    // For logging users in we need first to expose Spring Security's AuthenticationManager to other classes, so it can the autowired if needed.
    // Put this code to SecurityConfig.java:
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
