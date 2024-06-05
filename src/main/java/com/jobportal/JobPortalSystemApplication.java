package com.jobportal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@SpringBootApplication
@OpenAPIDefinition
@EnableTransactionManagement
public class JobPortalSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobPortalSystemApplication.class, args);
	}

}
