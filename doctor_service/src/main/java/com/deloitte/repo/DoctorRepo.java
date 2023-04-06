package com.deloitte.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.deloitte.entity.Doctor;

@Repository
public interface DoctorRepo extends MongoRepository<Doctor, String> {
	@Query("{'specialization':?0}")
    List<Doctor> findBySpecialization(String specialization);
}
