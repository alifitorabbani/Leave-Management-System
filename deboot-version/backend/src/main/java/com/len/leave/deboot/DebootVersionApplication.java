package com.len.leave.deboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Deboot Version Application
 * 
 * Main entry point for the Leave Management System using Flowable BPM.
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (opersero)
 */
@SpringBootApplication
@EnableJpaAuditing
public class DebootVersionApplication {

    public static void main(String[] args) {
        SpringApplication.run(DebootVersionApplication.class, args);
    }
}
