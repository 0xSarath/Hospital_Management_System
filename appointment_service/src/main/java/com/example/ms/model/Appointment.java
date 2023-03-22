package com.example.ms.model;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.deloitte.ms.entity.Doctor;

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

    private LocalDate date;

    @DBRef
    private Doctor doctor;

    @DBRef
    private Patient patient;

    private String reason;
}