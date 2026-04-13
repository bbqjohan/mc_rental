package com.sjoqvist.wigell_mc_rental.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DefaultSecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers("/actuator/**").permitAll()
                                        .requestMatchers("/api/v1/customers/**")
                                        .hasRole("ADMIN")
                                        .requestMatchers("/api/v1/bikes")
                                        .hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.PUT, "/api/v1/bookings/**")
                                        .hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.DELETE, "/api/v1/bookings/**")
                                        .hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.POST, "/api/v1/bookings")
                                        .hasRole("USER")
                                        .requestMatchers(HttpMethod.GET, "/api/v1/bookings")
                                        .hasAnyRole("USER", "ADMIN")
                                        .requestMatchers(HttpMethod.GET, "/api/v1/availability")
                                        .hasRole("USER")
                                        .anyRequest()
                                        .authenticated())
                .httpBasic(Customizer.withDefaults())
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        return http.build();
    }
}
