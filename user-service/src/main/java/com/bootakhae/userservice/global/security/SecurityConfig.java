package com.bootakhae.userservice.global.security;

import com.bootakhae.userservice.services.RefreshTokenService;
import com.bootakhae.userservice.services.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 인증 과정에서 사용하기 위한 AuthenticationManager 생성을 위한 로직
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.logout(AbstractHttpConfigurer::disable);
        http.headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.addFilter(getAuthenticationFilter(authenticationManager));

        http.authorizeHttpRequests(auth -> auth // 열어놓음;;
                .requestMatchers(new AntPathRequestMatcher("/**")
//                        new AntPathRequestMatcher("/api/v1/users/health-check", "GET"),
//                        new AntPathRequestMatcher("/api/v1/users/reissue", "GET"),
//                        new AntPathRequestMatcher("/api/v1/users/**", "POST"),
                                ).permitAll())
                .authenticationManager(authenticationManager);
//                .anyRequest().authenticated()).authenticationManager(authenticationManager);
//        http.exceptionHandling(c ->
//                c.authenticationEntryPoint((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//                        .accessDeniedHandler((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)));

        return http.build();
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception{
        AuthenticationFilter filter
                = new AuthenticationFilter(authenticationManager, tokenProvider, refreshTokenService);
        filter.setFilterProcessesUrl("/auth/sign-in"); // 로그인에 접근하기 위한 URL
        return filter;
    }
}
