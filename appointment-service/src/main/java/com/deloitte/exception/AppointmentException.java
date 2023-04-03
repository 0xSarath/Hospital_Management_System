package com.deloitte.exception;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AppointmentException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private int errorCode;
	private String errorMessage;

	public AppointmentException(String message) {
		super(message);
	}

	
	
	
}
