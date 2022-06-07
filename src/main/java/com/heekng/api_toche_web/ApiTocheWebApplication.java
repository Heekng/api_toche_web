package com.heekng.api_toche_web;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableBatchProcessing //스프링 배치 초기화 및 작동을 위한 빈, 설정
public class ApiTocheWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiTocheWebApplication.class, args);
	}

}
