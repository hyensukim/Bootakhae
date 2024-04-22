package com.bootakhae.webapp.config;

import com.bootakhae.webapp.jwt.AuthenticationFilter;
import com.bootakhae.webapp.jwt.JwtValidationFilter;
import com.bootakhae.webapp.user.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtValidationFilter jwtValidationFilter;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Environment env;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 인증 과정에서 사용하기 위한 AuthenticationManager 생성을 위한 로직
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(new AntPathRequestMatcher("/**"))
                    .access(new WebExpressionAuthorizationManager("hasIpAddress('222.99.81.232') or hasIpAddress('127.0.0.1')"))
                .requestMatchers(new AntPathRequestMatcher("/api/v1/users/**", "POST"),
                        new AntPathRequestMatcher("/auth/sign-in")).permitAll()
                .anyRequest().authenticated()).authenticationManager(authenticationManager);

        http.addFilter(getAuthenticationFilter(authenticationManager))
            .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http.exceptionHandling(c ->
                c.authenticationEntryPoint((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                        .accessDeniedHandler((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)));

        return http.build();
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception{
        AuthenticationFilter filter = new AuthenticationFilter(authenticationManager, userService, env);
        filter.setFilterProcessesUrl("/auth/sign-in"); // 로그인에 접근하기 위한 URL
        return filter;
    }
}
