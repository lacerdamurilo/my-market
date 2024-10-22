package br.edu.ifrs.restinga.market.mymarket.config;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers("/admin/**").authenticated()
                                .requestMatchers("/products/create").authenticated()
                                .requestMatchers("/products/update/**").authenticated()
                                .requestMatchers("/products/delete/**").authenticated()
                                .anyRequest().permitAll()
                )
                .httpBasic(basic -> basic.authenticationEntryPoint(
                        (_, response, _) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                ))
                .userDetailsService(userDetailsService)
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
