package com.gs310.bookstracker;

import com.gs310.bookstracker.account.CustomAuthenticationFailureHandler;
import com.gs310.bookstracker.account.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ProjectConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomOAuth2UserService customOAuth2UserService, CustomAuthenticationFailureHandler authenticationFailureHandler) throws Exception {
        http
                .authorizeHttpRequests((req) -> {req
                        .requestMatchers("/", "index.html", "/register", "/sign-up").permitAll() // do not need to authenticate "/"
                        .requestMatchers("/books/**").authenticated()
                        .anyRequest().authenticated();
                })
//                .csrf(c -> c.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .formLogin(form -> form
                        .loginPage("/")
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
