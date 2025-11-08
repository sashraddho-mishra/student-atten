package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(org.springframework.security.core.userdetails.UserDetailsService userDetailsService,
                          org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/h2-console/**", "/register", "/css/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(f -> f.loginPage("/login").defaultSuccessUrl("/teacher/dashboard", true).permitAll())
                .logout(l -> l.logoutSuccessUrl("/login?logout").permitAll())
                .csrf(c -> c.ignoringRequestMatchers("/h2-console/**"))
                .headers(h -> h.frameOptions().disable());
        return http.build();
    }
}
