package com.gs310.bookstracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.gs310.bookstracker.account.CustomAuthenticationFailureHandler;
import com.gs310.bookstracker.account.CustomOAuth2UserService;

@Configuration
public class ProjectConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService, CustomAuthenticationFailureHandler authenticationFailureHandler) throws Exception {
        http
                .authorizeHttpRequests((req) -> {req
                        .requestMatchers("/", "/register", "/sign-up").permitAll() // do not need to authenticate "/"
                        .anyRequest().authenticated();
                })
//                .csrf(c -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginProcessingUrl("/login") // This is the endpoint that is used to process the login. 
                        .defaultSuccessUrl("/home", true) // Redirect on successful login. 
                        .failureHandler(authenticationFailureHandler) // Redirect on failed login. 
                        .permitAll()
                )
                .oauth2Login(Customizer.withDefaults())
                .oauth2Login( oauth -> { oauth
                        .userInfoEndpoint(userInfoEndpoint ->
                            userInfoEndpoint
                                .userService(customOAuth2UserService)
                        )
                        .defaultSuccessUrl("/home", true); // if successful login, take to user's home page
                })
                .logout(l -> l
                    .logoutSuccessUrl("/").permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    public CompromisedPasswordChecker compromisedPasswordChecker() {
//        return new HaveIBeenPwnedRestApiPasswordChecker();
//    }

}
