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
@Document("patients")
public class Patient {

	@Id
	private String id;
	@NotNull(message = "Name cannot be empty")
	private String name;
	@NotNull(message = "Please specify the gender")
	private String gender;
	@NotNull(message = "Age cannot be empty")
	private int age;
	@NotNull(message = "Email cannot be empty")
	@Email(message = "Enter a valid Email")
	private String email;
}