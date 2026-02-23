package com.len.leave.flowable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main Application Class for Flowable Version Backend
 * 
 * This application implements a Leave Management System using Flowable BPM Engine.
 * The workflow is defined in BPMN 2.0 XML and managed by the Flowable engine.
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (eiros)
 * @context Backend Engineer Intern - Workflow Research
 */
@SpringBootApplication
@EnableJpaAuditing
public class FlowableVersionApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowableVersionApplication.class, args);
    }
}
