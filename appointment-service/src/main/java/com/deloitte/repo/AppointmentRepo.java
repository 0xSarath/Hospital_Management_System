package com.deloitte.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.deloitte.model.Appointment;

@Repository
public interface AppointmentRepo extends MongoRepository<Appointment, String> {
    @Query("{'reason' : ?0}")
	List<Appointment> findByReason(String reason);
}
