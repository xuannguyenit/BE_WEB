package com.xuannguyen.oder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OderServiceApplication.class, args);
	}

}
