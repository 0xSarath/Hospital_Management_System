package com.deloitte.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.deloitte.model.Appointment;

@Repository
public interface AppointmentRepo extends MongoRepository<Appointment, String> {

}
