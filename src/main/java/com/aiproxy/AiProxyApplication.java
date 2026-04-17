package com.aiproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AiProxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiProxyApplication.class, args);
    }
}

