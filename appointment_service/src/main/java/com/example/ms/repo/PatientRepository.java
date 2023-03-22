package com.example.ms.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ms.model.Patient;

public interface PatientRepository extends MongoRepository<Patient, String>{

}
