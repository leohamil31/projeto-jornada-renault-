package com.renault.lablean.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desabilita a proteção CSRF, que é o padrão para APIs REST que vão usar tokens
                .csrf(csrf -> csrf.disable())
                
                // Configura a API para ser Stateless (não guarda sessão na memória, essencial para JWT depois)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                
                // Regras de autorização das rotas
                .authorizeHttpRequests(auth -> auth
                        // Temporariamente, permite acesso total a todas as rotas (incluindo o Swagger) sem senha
                        .anyRequest().permitAll()
                )
                .build();
    }
}