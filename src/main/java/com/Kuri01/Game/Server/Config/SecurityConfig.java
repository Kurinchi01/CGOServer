package com.Kuri01.Game.Server.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF für stateless APIs deaktivieren
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Keine Sessions

                // NEU: Konfiguration für die Frame-Options, damit die H2-Console funktioniert
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                .authorizeHttpRequests(auth -> auth
                        // NEU: Erlaube den Zugriff auf die H2-Console für JEDEN
                        .requestMatchers("/h2-console/**").permitAll()
                        // Erlaube den Zugriff auf alle unsere Authentifizierungs-Endpunkte
                        .requestMatchers("/api/auth/**").permitAll()
                        // Alle anderen Anfragen erfordern eine Authentifizierung
                        .anyRequest().hasRole("USER")
                )
                // JWT Filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);



        return http.build();
    }

    // Wir müssen Spring auch sagen, wie es den UserDetailsService findet.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // Da wir keine Passwörter verwenden, brauchen wir keinen PasswordEncoder
        return new DaoAuthenticationProvider(userDetailsService);
    }
}