package com.bootakhae.userservice.global.config;


import com.bootakhae.userservice.global.jwt.AuthenticationFilter;
import com.bootakhae.userservice.global.jwt.JwtValidationFilter;
import com.bootakhae.userservice.global.jwt.TokenProvider;
import com.bootakhae.userservice.services.TokenService;
import com.bootakhae.userservice.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
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

    private final JwtValidationFilter jwtValidationFilter;
    private final UserService userService;
    private final TokenService tokenService;
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

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
//                        new AntPathRequestMatcher("/api/v1/users/health-check", "GET"),
                        new AntPathRequestMatcher("/api/v1/users/reissue", "GET"),
                        new AntPathRequestMatcher("/api/v1/users/**", "POST"),
                        new AntPathRequestMatcher("/auth/sign-in")).permitAll()
                .anyRequest().authenticated()).authenticationManager(authenticationManager);

        http.addFilterBefore(jwtValidationFilter, AuthenticationFilter.class)
                .addFilter(getAuthenticationFilter(authenticationManager));

        http.headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http.exceptionHandling(c ->
                c.authenticationEntryPoint((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                        .accessDeniedHandler((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)));

        return http.build();
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception{
        AuthenticationFilter filter = new AuthenticationFilter(authenticationManager, tokenService, tokenProvider);
        filter.setFilterProcessesUrl("/auth/sign-in"); // 로그인에 접근하기 위한 URL
        return filter;
    }
}
