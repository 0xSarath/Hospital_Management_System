package com.example.ms.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.ms.model.Appointment;

@Repository
public interface AppointmentRepo extends MongoRepository<Appointment, String>{

}
