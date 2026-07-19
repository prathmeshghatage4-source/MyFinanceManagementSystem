package com.example.MyExpenseTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MyExpenseTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyExpenseTrackerApplication.class, args);
	}

}
