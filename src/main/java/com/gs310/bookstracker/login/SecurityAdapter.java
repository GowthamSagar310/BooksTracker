package com.gs310.bookstracker.login;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityAdapter {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((req) -> {req
                        .requestMatchers("/").permitAll() // do not need to authenticate "/"
                        .anyRequest().authenticated();
                })
                .csrf(c -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .httpBasic(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults())
                .oauth2Login( oauth -> { oauth
                    .defaultSuccessUrl("/user", true); // if successful login, take to user's home page
                })
                .logout(l -> l
                        .logoutSuccessUrl("/").permitAll()
                );
        return http.build();
    }

}
