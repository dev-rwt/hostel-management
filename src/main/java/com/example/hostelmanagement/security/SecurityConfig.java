package com.example.hostelmanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import com.example.hostelmanagement.service.AppUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final JwtCookieFilter jwtCookieFilter;

    public SecurityConfig(JwtCookieFilter jwtCookieFilter) {
        this.jwtCookieFilter = jwtCookieFilter;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless authentication
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login", "/auth/register").permitAll() // Public Endpoints
                .requestMatchers("/admin/**").hasRole("ADMIN") // 🔹 Only ADMINs can access "/admin/**"
                .requestMatchers("/student/**").hasRole("STUDENT") // 🔹 Only STUDENTs can access "/student/**"
                .anyRequest().authenticated() // All other requests require authentication
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))); // 🔹 Custom JWT Role Converter
        http.addFilterBefore(jwtCookieFilter, SecurityContextHolderFilter.class);
        http.exceptionHandling(ex -> 
        ex.authenticationEntryPoint((request, response, authException) -> 
            response.sendRedirect("/auth/login") // Redirect to login page if unauthorized
        )
    );
        return http.build();
    }
    

    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt for password encoding
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey key =  Keys.hmacShaKeyFor(Decoders.BASE64.decode("qzKX4D0JlQ2n0LbEp3Uj9LtQo2J8fKZT9As5xJ1Yr2E="));
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_"); // 🔹 Prefix for Spring Security roles
        grantedAuthoritiesConverter.setAuthoritiesClaimName("role"); // 🔹 Extract role from JWT claim

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return authenticationConverter;
    }
}





