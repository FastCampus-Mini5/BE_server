package com.example.core.config._security;

import com.example.core.config._security.encryption.AESEncryption;
import com.example.core.config._security.encryption.Encryption;
import com.example.core.config._security.jwt.JwtAuthenticationFilter;
import com.example.core.errors.ErrorMessage;
import com.example.core.errors.exception.Exception401;
import com.example.core.errors.exception.Exception403;
import com.example.core.util.FilterResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

    @Bean
    public Encryption encryption(Environment environment) {
        return new AESEncryption(environment);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .headers().frameOptions().sameOrigin()

                .and().cors().configurationSource(configurationSource())

                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and().formLogin().disable()

                .httpBasic().disable()

                .apply(new SecurityFilterManager())

                .and().exceptionHandling().authenticationEntryPoint(
                        (request, response, authException) ->
                                FilterResponse.unAuthorized(response, new Exception401(ErrorMessage.UN_AUTHORIZED)))

                .and().exceptionHandling().accessDeniedHandler(
                        (request, response, accessDeniedException) ->
                                FilterResponse.forbidden(response, new Exception403(ErrorMessage.FORBIDDEN)))

                .and().authorizeRequests(
                        expressionInterceptUrlRegistry ->
                                expressionInterceptUrlRegistry
                                        .antMatchers("/api/admin/user/signIn").permitAll()
                                        .antMatchers("/api/admin/**").hasRole("ADMIN")
                                        .anyRequest().permitAll());

        return http.build();
    }

    private CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    public static class SecurityFilterManager
            extends AbstractHttpConfigurer<SecurityFilterManager, HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager =
                    builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new JwtAuthenticationFilter(authenticationManager));
            super.configure(builder);
        }
    }
}
