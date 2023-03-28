package com.deloitte.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.entity.Doctor;

@Repository
public interface DoctorRepo extends MongoRepository<Doctor, String> {
    Doctor findBySpecialization(String specialization);
}
