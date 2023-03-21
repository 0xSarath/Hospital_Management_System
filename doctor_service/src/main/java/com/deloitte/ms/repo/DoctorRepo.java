package com.deloitte.ms.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.ms.entity.Doctor;

@Repository
public interface DoctorRepo extends MongoRepository<Doctor, String> {
    List<Doctor> findBySpecialization(String specialization);
    Optional<Doctor> findByEmail(String email);
}
