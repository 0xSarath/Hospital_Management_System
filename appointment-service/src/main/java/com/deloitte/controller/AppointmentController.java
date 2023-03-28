package com.deloitte.controller;

import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.PUT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.model.Appointment;
import com.deloitte.model.Patient;
import com.deloitte.service.AppointmentService;

@RestController
@RequestMapping("/api")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

	private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

	@GetMapping("/getAllDoctors")
	public ResponseEntity<Object> getAllDoctors() {
		logger.info("Getting doctor details ");
		Object doctors = appointmentService.getAllDoctorDetails();
		logger.info("Doctor Details: {} ", doctors);
		return ResponseEntity.ok(doctors);
	}

	@GetMapping("/appointments")
	public ResponseEntity<List<Appointment>> getAllAppointments() {
		logger.info("Getting Appointments");
		List<Appointment> appointments = appointmentService.findAllAppointments();
		logger.info("Appointments so far: {}",appointments);
		return ResponseEntity.ok(appointments);
	}

	@GetMapping("/")
	public ResponseEntity<List<Appointment>> getAppointmentsByDoctorId(@RequestParam("id") String doctorId) {
		logger.info(" Getting Appointments by DoctorId: {}",doctorId);
		List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
		logger.info("Appointments By DoctorId",appointments);
		return ResponseEntity.ok(appointments);
	}

	@PostMapping("/appointments")
	public ResponseEntity<Appointment> createAppointment(@Valid @RequestBody Appointment appointment) throws Exception {
		logger.info("Appointment Creation Started");
		Appointment appointment1 = appointmentService.createAppointment(appointment);
		logger.info("Created appointment Successfully: {}", appointment1);

		return ResponseEntity.ok(appointment1);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Appointment> updateAppointmentDate(@Valid @PathVariable(value = "id") String id,
			@Valid @RequestBody LocalDate date) throws Exception {
		logger.info("Updating Appointment Details");
		Appointment appointment = appointmentService.updateAppointmentDate(id, date);
		logger.info("Appointment Details updated Successfully: {}", appointment);
		return ResponseEntity.ok(appointment);
	}

	@PutMapping("/appointment/patient")
	public ResponseEntity<Appointment> updatePatientDetailsByAppointmentID(@RequestParam("id") String appointmentId,
			@RequestBody Patient patient) throws Exception {
		logger.info("Updating the patientDetails");
		Appointment appointment = appointmentService.updatePatientDetailsByAppointmentID(appointmentId, patient);
		logger.info("Patient details updated Successfully", appointment);
		return ResponseEntity.ok(appointment);
	}

	@DeleteMapping("/appointment/{id}")
	public ResponseEntity<String> deleteAppointmentByID(@Valid @PathVariable(value = "id") String appointmentId)
			throws Exception {
		logger.info("Deleting the Appointment with id : {}",appointmentId);
		String appointment = appointmentService.deleteAppointmentByID(appointmentId);
		logger.info("Appointment Deleted Successfully");
		return ResponseEntity.ok(appointment);
	}
	

}
