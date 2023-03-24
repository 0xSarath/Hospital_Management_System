package com.example.ms.repo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.ms.model.Appointment;

@Repository
public interface AppointmentRepo extends MongoRepository<Appointment, String>{
     List<Appointment> getAppointmentsByDoctor(String doctorId);
}
