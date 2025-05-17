package com.app.QuizService.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${key.jwt.value}")
    String KEY;

    String[] publicEntryPoint = {"/**"};



    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(publicEntryPoint)
                        .permitAll().anyRequest()
                        .authenticated())

                .oauth2ResourceServer(oath -> oath
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new AuthEntryPointCustom()))

                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec spec = new SecretKeySpec(KEY.getBytes(),"HS256");
        return NimbusJwtDecoder
                .withSecretKey(spec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder(10);
    };
}
