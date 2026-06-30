package com.meta12.SS8911.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // ★ SiteUserService 주입 없음 → 순환참조 없음
    // Spring Security가 UserDetailsService 구현체(SiteUserService)를 자동으로 찾아서 씀

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/siteUser/login",
                                "/siteUser/chuga",
                                "/siteUser/chugaProc",
                                "/notices",
                                "/community/**",
                                "/faq",
                                "/lectures",
                                "/lectures/**",
                                "/game",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/fonts/**",
                                "/ws/chat/**",
                                "/api/chat/history"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/siteUser/login")
                        .loginProcessingUrl("/siteUser/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/siteUser/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                )
                .csrf(csrf -> csrf
                        // WebSocket 핸드셰이크는 STOMP 프레임 자체로 인증되므로 CSRF 토큰 검사에서 제외
                        .ignoringRequestMatchers("/ws/chat/**")
                );
        // ★ csrf.disable() 제거 → CSRF 기본 활성화 (다른 요청에는 그대로 적용됨)

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}