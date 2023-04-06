package com.deloitte.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModelProperty;
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
	@Pattern(regexp = "^[0-9]*$", message = "Only Numbers are allowed!!")
	private String id;

	@NotBlank(message = "Name cannot be empty")
	@ApiModelProperty(hidden = true)
	@Pattern(regexp = "^[A-Za-z]*$", message = "Only alphabets are allowed!!")
	private String name;

	@NotNull(message = "Please enter the email")
	@Email(message = "Enter a valid email")
	@Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
			+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
	@ApiModelProperty(hidden = true)
	private String email;

	@NotNull(message = "Specialization cannot be empty")
	@ApiModelProperty(hidden = true)
	private String specialization;
}