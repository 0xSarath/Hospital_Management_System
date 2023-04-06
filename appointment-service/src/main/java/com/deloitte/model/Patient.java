package com.deloitte.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
	@Pattern(regexp = "^[0-9]*$",message = "Only Numbers are allowed!!")
	private String id;
	@NotEmpty(message = "Name cannot be empty")
	@Pattern(regexp = "^[A-Za-z]*$",message = "Only alphabets are allowed!!")
	private String name;
	@NotNull(message = "Please specify the gender")
	private String gender;
	@NotNull(message = "Age cannot be empty")
	@Size(min = 1,message = "minimum age should be 1")
	private int age;	
	@NotNull(message = "Email cannot be empty")
	@Email(message = "Enter a valid Email")
	private String email;
}