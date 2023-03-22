package com.example.ms.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.ms.model.Patient;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String>{

}
