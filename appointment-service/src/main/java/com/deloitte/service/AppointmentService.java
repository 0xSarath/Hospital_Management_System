package com.deloitte.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.deloitte.exception.DoctorException;
import com.deloitte.model.Appointment;
import com.deloitte.model.Patient;

@Service
public interface AppointmentService {
	
	ResponseEntity<List<Appointment>> getAppointmentsByDoctor(String doctorId);

	Object getAllDoctorDetails() throws DoctorException;

	ResponseEntity<String> deleteAppointmentByID(String appointmentId) throws Exception;

	ResponseEntity<Appointment> createAppointment(Appointment appointment) throws Exception;

	ResponseEntity<Appointment> updateAppointmentDate(String id, LocalDate date) throws Exception;

	ResponseEntity<Appointment> updatePatientDetailsByAppointmentID(String appointmentId, Patient patient) throws Exception;

	ResponseEntity<List<Appointment>> findAllAppointments();
}
