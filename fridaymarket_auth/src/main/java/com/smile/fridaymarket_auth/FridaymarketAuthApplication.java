package com.smile.fridaymarket_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class FridaymarketAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(FridaymarketAuthApplication.class, args);
	}

}
