package com.example.demo20250620;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@ServletComponentScan
@SpringBootApplication

public class Demo20250620Application {

	public static void main(String[] args) {
		SpringApplication.run(Demo20250620Application.class, args);
	}

}
