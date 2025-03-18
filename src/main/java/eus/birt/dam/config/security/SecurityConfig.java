package eus.birt.dam.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private Environment environment;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        // Verifica si estamos en producción (Railway)
        boolean isProd = Arrays.asList(environment.getActiveProfiles()).contains("prod");
        
        // Configurar endpoints públicos y privados
        http
            .authorizeHttpRequests(auth -> {
                // Endpoints públicos (login, registro, health check)
                auth.requestMatchers("/auth/**", "/api/usuarios/registro", "/health").permitAll();
                auth.requestMatchers("/api/diagnostic").permitAll();
                
                // Las películas requieren autenticación (restaurando comportamiento original)
                auth.requestMatchers("/api/peliculas/**").authenticated();
                
                // Si no estamos en producción, permitir H2 Console
                if (!isProd) {
                    auth.requestMatchers(request -> {
                        try {
                            return PathRequest.toH2Console().matches(request);
                        } catch (Exception e) {
                            return false;
                        }
                    }).permitAll();
                }
                
                // Todo lo demás requiere autenticación
                auth.anyRequest().authenticated();
            })
            // Deshabilitar CSRF para APIs REST
            .csrf(csrf -> csrf.disable())
            
            // Usar modo STATELESS para APIs REST
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
            
        // Configuración de frameOptions solo para entornos no-prod
        if (!isProd) {
            try {
                http.headers(headers -> 
                    headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                );
            } catch (Exception e) {
                System.out.println("No se pudo configurar frameOptions: " + e.getMessage());
            }
        }

        // Añadir filtro JWT mejorado
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
