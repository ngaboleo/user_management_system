package com.user.user_management_system.user.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig{
    @Autowired
    private final UserImplDetailService userImplDetailService;
    @Autowired
    private final AuthenticationFilter authenticationFilter;
    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
//        disable the framing protection that is implemented by the browser.
        http.headers().frameOptions().disable();

        http.cors().and().csrf().disable().
                authorizeHttpRequests()
                    .requestMatchers("/public/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
