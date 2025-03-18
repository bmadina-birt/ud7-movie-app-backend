package eus.birt.dam.config.security;


import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter, HandlerMappingIntrospector introspector) throws Exception {

        // Configuramos los endpoints públicos y privados
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/api/usuarios/registro", "/api/diagnostic", "/health").permitAll()
                // Configuración condicional de H2 Console
                .requestMatchers(PathRequest.toH2Console())
                    .permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf
                // Configuración condicional de H2 Console
                .ignoringRequestMatchers(PathRequest.toH2Console())
                .disable()
            );
        
        // Configuración condicional de H2 Console
        try {
            http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        } catch (Exception e) {
            // Si H2 Console no está disponible, simplemente ignoramos esta configuración
            System.out.println("H2 Console no está disponible en este entorno.");
        }

        // Añadir filtro JWT
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
