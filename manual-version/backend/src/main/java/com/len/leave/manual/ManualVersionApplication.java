package com.len.leave.manual;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main Application Class for Manual Version Backend
 * 
 * This application implements a Leave Management System using manual workflow
 * with state machine logic (hardcoded transitions, enum-based status).
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (opersero)
 * @context Backend Engineer Intern - Workflow Research
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
@EnableAspectJAutoProxy
public class ManualVersionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManualVersionApplication.class, args);
    }
}
