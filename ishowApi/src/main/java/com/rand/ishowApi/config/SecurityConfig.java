package com.rand.ishowApi.config;


import com.rand.ishowApi.security.filter.MobileDeviceFilter;
import com.rand.ishowApi.security.filter.UserFilter;
import com.rand.ishowApi.security.jwt.JwtAuthConverter;
import com.rand.ishowApi.security.jwt.JwtAuthEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthConverter jwtAuthConverter;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final UserFilter userFilter;
    private final MobileDeviceFilter mobileDeviceFilter;


    private static final String[] PUBLIC_ENDPOINTS = {
            "/public/**",
            "/api/v1/public/**",
            "/api/v1/mobile/auth/**",
            "/api/v1/admin/auth/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/v1/stream/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthConverter)
                        ).authenticationEntryPoint(jwtAuthEntryPoint)
                )
                .addFilterAfter(userFilter, BearerTokenAuthenticationFilter.class)
                .addFilterAfter(mobileDeviceFilter, UserFilter.class);

        return http.build();
    }

}
