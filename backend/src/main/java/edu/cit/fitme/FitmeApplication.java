package edu.cit.fitme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMethodSecurity
public class FitmeApplication {
	public static void main(String[] args) {
		SpringApplication.run(FitmeApplication.class, args);
	}
}