package com.inventory_pro.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**", "/js/**", "/image/**").permitAll() // Public assets
                        .requestMatchers("/admin/**").hasAuthority("ADMIN") // Change hasRole to hasAuthority
                        .requestMatchers(
                                "/api/**",
                                "/products/**",
                                "/categories/**",
                                "/stocks/**",
                                "/users/**",
                                "/sales/**",
                                "/reports/**",
                                "/settings/**"
                        )
                        .authenticated() // Any logged-in user
                        .anyRequest().authenticated() // Everything else needs login
                )
                .formLogin(form -> form
                        .loginPage("/login") // Your custom login page
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll())
                .logout(logout -> logout.permitAll());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Matches your DB comment
    }
}