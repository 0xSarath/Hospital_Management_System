package com.deloitte.model;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
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
@Document(collection = "appointments")
public class Appointment {
    @Id
    @ApiModelProperty(position = 1)
    @Size(min = 3,max = 5)
    private String id;
    
    
    @NotNull(message = "Please mention the Date in YYY-MM-DD Format")
    @ApiModelProperty(position = 2)
    private LocalDate date;
    
    @DBRef
    @NotNull(message = "Please mention the doctor")
    @ApiModelProperty(position = 3)
    private Doctor doctor;
    
    @DBRef
    @NotNull(message = "Please enter the patient details")
    @Valid
    @ApiModelProperty(position = 4)
    private Patient patient;
    
    @Pattern(regexp = "^[A-Za-z]*$",message = "Reason should be max 30 Characters")
    @ApiModelProperty(position = 5)
    private String reason;
}