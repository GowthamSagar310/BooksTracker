package com.gs310.bookstracker;

//import com.gs310.bookstracker.dataconnection.DataStaxDBProps;

import com.gs310.bookstracker.account.UserEntity;
import com.gs310.bookstracker.account.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BooksTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooksTrackerApplication.class, args);
    }

//    @Bean
//    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxDBProps astraProperties) {
//        Path bundle = astraProperties.getSecureConnectBundle().toPath();
//        return builder -> builder.withCloudSecureConnectBundle(bundle);
//    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if(!userRepository.findAll().iterator().hasNext()){
                UserEntity user = new UserEntity();
                user.setUsername("admin");
                user.setEmail("test@test.com");
                user.setPassword(passwordEncoder.encode("dummy"));
                user.setRole("USER");
                userRepository.save(user);
            }
        };
    }

}
