package com.deloitte.ms.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Doctors")
public class Doctor  {
    @Id
    private String id;
    private String name;

    private String email;

    private String specialization;
}