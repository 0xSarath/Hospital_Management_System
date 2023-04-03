package com.deloitte;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

import com.deloitte.repo.AppointmentRepo;
import com.deloitte.repo.PatientRepository;

@SpringBootApplication
@ComponentScan(basePackages = { "com.deloitte" })
@EnableMongoRepositories(basePackageClasses = { AppointmentRepo.class, PatientRepository.class })
public class AppointmentServiceApplication {
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(AppointmentServiceApplication.class, args);
	}
}
