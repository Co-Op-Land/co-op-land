package com.coop.global.config;

import com.coop.domain.member.enums.Role;
import com.coop.global.security.CustomAccessDeniedHandler;
import com.coop.global.security.CustomAuthEntryPoint;
import com.coop.global.security.JwtFilter;
import com.coop.global.security.JwtSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtSecurityProperties jwtSecurityProperties;
    private final CustomAuthEntryPoint customAuthEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) //csrf 비활성화
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //세션 사용 안함
                .addFilterAt(jwtFilter, SecurityContextHolderAwareRequestFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                                .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.from("1; mode=block"))) //XSS 공격 방지
                                .contentTypeOptions(HeadersConfigurer.ContentTypeOptionsConfig::disable) //MIME 스니핑 방지
                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin) //ClickJacking 방지
                                .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER)) //Referrer 비활성화
                                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; script-src 'self' 'nonce-randomValue'"))//XSS 방지
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(jwtSecurityProperties.secret().whiteList().toArray(new String[0])).permitAll()
                        .requestMatchers(jwtSecurityProperties.secret().adminList().toArray(new String[0])).hasAuthority(Role.Authority.ADMIN)
                        .anyRequest().hasAnyAuthority(Role.Authority.ADMIN, Role.Authority.USER)
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
