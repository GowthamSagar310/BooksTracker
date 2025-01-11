package com.gs310.bookstracker;

//import com.gs310.bookstracker.dataconnection.DataStaxDBProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.nio.file.Path;

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

}
