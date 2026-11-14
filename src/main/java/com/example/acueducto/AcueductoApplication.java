package com.example.acueducto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AcueductoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcueductoApplication.class, args);
    }
}