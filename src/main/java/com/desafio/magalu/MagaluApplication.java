package com.desafio.magalu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MagaluApplication {

	public static void main(String[] args) {
		SpringApplication.run(MagaluApplication.class, args);
	}

}
