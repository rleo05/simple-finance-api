package com.project.simple_finance_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication()
public class SimpleFinanceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleFinanceApiApplication.class, args);
	}

}
