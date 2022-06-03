package com.heekng.api_toche_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ApiTocheWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiTocheWebApplication.class, args);
	}

}
