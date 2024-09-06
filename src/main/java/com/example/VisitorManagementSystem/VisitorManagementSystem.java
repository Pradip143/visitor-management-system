package com.example.VisitorManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VisitorManagementSystem {

	public static void main(String[] args) {
		SpringApplication.run(VisitorManagementSystem.class, args);
	}

}
