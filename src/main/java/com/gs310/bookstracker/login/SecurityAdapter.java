package com.gs310.bookstracker.login;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityAdapter {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((req) -> {req
                        .requestMatchers("/").permitAll() // do not need to authenticate "/"
                        .anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults())
                .oauth2Login( oauth -> { oauth
                    .defaultSuccessUrl("/user", true);
                });
        return http.build();
    }

}
