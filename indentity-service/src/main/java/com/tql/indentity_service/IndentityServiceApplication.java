package com.tql.indentity_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IndentityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IndentityServiceApplication.class, args);
	}

}
