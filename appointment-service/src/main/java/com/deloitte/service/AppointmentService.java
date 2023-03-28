package com.deloitte.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import com.deloitte.model.Appointment;
import com.deloitte.model.Patient;

@Service
public interface AppointmentService {
	
	List<Appointment> getAppointmentsByDoctor(String doctorId);

	Object getAllDoctorDetails();

	String deleteAppointmentByID(String appointmentId) throws Exception;

	Appointment createAppointment(Appointment appointment) throws Exception;

	Appointment updateAppointmentDate(String id, LocalDate date) throws Exception;

	Appointment updatePatientDetailsByAppointmentID(String appointmentId, Patient patient) throws Exception;

	List<Appointment> findAllAppointments();
}
