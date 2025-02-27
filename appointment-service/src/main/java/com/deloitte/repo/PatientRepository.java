package com.deloitte.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.model.Patient;

@Repository
public interface PatientRepository extends MongoRepository<Patient, String>{

}
