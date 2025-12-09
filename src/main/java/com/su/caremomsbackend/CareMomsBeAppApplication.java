package com.su.caremomsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.su.caremomsbackend")
@EnableJpaRepositories(basePackages = "com.su.caremomsbackend.repository")
@EntityScan(basePackages = "com.su.caremomsbackend.model")
public class CareMomsBeAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CareMomsBeAppApplication.class, args);
    }
}
