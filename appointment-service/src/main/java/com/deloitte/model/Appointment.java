package com.deloitte.model;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "appointments")
public class Appointment {
    @Id
    private String id;
    
    @NotNull(message = "Please mention the date")
    private LocalDate date;
    
    @DBRef
    @NotNull(message = "Please mention the doctor")
    private Doctor doctor;
    
    @DBRef
    @NotNull(message = "Please enter the patient details")
    private Patient patient;
    
    @NotNull(message = "Please mention the reason")
    private String reason;
}