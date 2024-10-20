package com.brokeragefirm.common.config.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.cors(withDefaults()).csrf(AbstractHttpConfigurer::disable).httpBasic(withDefaults())
        .authorizeHttpRequests(
            authentication -> authentication
                .requestMatchers("/actuator/**", "swagger-ui.html", "/swagger-ui/**",
                    "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll()
                .anyRequest().authenticated())
        .build();
  }
}
