package com.ra.projectmd05.security;

import com.ra.projectmd05.security.jwt.JwtAuthTokenFilter;
import com.ra.projectmd05.security.jwt.JwtEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private JwtAuthTokenFilter jwtAuthTokenFilter;
    @Autowired
    private JwtEntryPoint jwtEntryPoint;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.cors(cf->cf.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:5173/","http://localhost:5174/","http://localhost:5175/"));
//                    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                    config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "Accept"));
                    config.setAllowCredentials(true);
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                    config.setExposedHeaders(List.of("*"));
                    return config;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth->{
//                    auth.requestMatchers("/api/v1/admin/auth").hasAnyAuthority("ADMIN","SUB_ADMIN");
//                    auth.requestMatchers("/api/v1/admin/auth/add").hasAuthority("ADMIN");
                    auth.requestMatchers("/api/v1/superAdmin/**").hasAuthority("ROLE_SUPER_ADMIN");
                    auth.requestMatchers("/api/v1/admin/**").hasAuthority("ROLE_ADMIN");
                    auth.requestMatchers("/api/v1/user/**").hasAuthority("ROLE_USER");
//                    auth.requestMatchers("/api/v1/user/**").authenticated();

                    auth.anyRequest().permitAll();
                }).sessionManagement(auth->auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
                exceptionHandling(auth->auth.authenticationEntryPoint(jwtEntryPoint)).
                addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class).
                build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
}
