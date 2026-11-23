package com.controleportaria.security;

import com.controleportaria.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
public class WebSecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/error", "/bootstrap-5.1.3-dist/**", "/style/**", "/js/**", "/favicon.svg").permitAll()
                .requestMatchers("/usuarios/**", "/entradas/excluir/**", "/entradas/editar/**",
                    "/entradas/export/**", "/objetos/excluir/**", "/objetos/editar/**")
                    .hasAuthority(Role.ADMIN.name())
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("user_nome")
                .passwordParameter("user_senha")
                .defaultSuccessUrl("/home", true)
                .permitAll())
            .logout(Customizer.withDefaults())
            .rememberMe(Customizer.withDefaults());
        // A protecao CSRF continua ativa (padrao do Spring Security).
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
