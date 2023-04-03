package com.deloitte.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document("Doctors")
public class Doctor {
	@Id
	@NotNull(message = "Doctor Id cannot be Empty")
	private String id;
	
	@NotNull(message = "Name cannot be empty")
	private String name;
	
	@NotNull(message = "Please enter the email")
	@Email(message = "Enter a valid email")
	private String email;
	
	@NotNull(message = "Specialization cannot be empty")
	private String specialization;
}