package com.xuannguyen.notifycation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NotifycationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotifycationServiceApplication.class, args);
	}

}
