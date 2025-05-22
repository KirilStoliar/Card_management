package com.stoliar.cardManagement.config;

import com.stoliar.cardManagement.service.JwtAuthService;
import com.stoliar.cardManagement.service.JwtService;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    @Bean
    public JwtService jwtService() {
        // Генерация надежного ключа
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        return new JwtService(Encoders.BASE64.encode(key.getEncoded()), expiration);
    }

    @Bean
    public JwtAuthService jwtAuthService(JwtService jwtService,
                                         UserDetailsService userDetailsService) {
        return new JwtAuthService(jwtService, userDetailsService);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtAuthService jwtAuthService) {
        return new JwtAuthenticationFilter(jwtAuthService);
    }
}