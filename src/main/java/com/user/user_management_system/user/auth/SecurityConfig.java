package com.user.user_management_system.user.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig{
    @Autowired
    private final UserImplDetailService userImplDetailService;
    @Autowired
    private final AuthenticationFilter authenticationFilter;

    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
        http.
    }

}
