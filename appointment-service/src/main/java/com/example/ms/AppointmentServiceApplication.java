package com.example.ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import com.example.ms.repo.AppointmentRepo;
import com.example.ms.repo.PatientRepository;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {"com.example.ms"})
@EnableMongoRepositories(basePackageClasses = { AppointmentRepo.class,PatientRepository.class})
public class AppointmentServiceApplication {
	
	//Creating swagger for appointmentService
	@Bean(name = "appointmentServiceApi")
    public Docket appointmentServiceApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)).paths(PathSelectors.any())
				.build().groupName("Appointment Service API")
				.host("localhost:3002").
				apiInfo(apiInfo1()).useDefaultResponseMessages(false);
	}

	@Bean
	public ApiInfo apiInfo1() {
		final ApiInfoBuilder builder = new ApiInfoBuilder();
		return builder.build();
	}
	
		@Bean
		public RestTemplate restTemplate() {
			return new RestTemplate();
		}
	public static void main(String[] args) {
		SpringApplication.run(AppointmentServiceApplication.class, args);
	}

}
