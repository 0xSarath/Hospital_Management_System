package com.example.ms.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import javax.ws.rs.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.ms.model.Appointment;
import com.example.ms.model.Doctor;
import com.example.ms.model.Patient;
import com.example.ms.repo.AppointmentRepo;
import com.example.ms.repo.PatientRepository;

@RestController
@RequestMapping("/api")
public class AppointmentController {

	@Autowired
	private AppointmentRepo appointmentRepository;
	@Autowired
	private PatientRepository patientRepository;

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/getAllDoctors")
	public List<Object> getAllDoctorDetails() {

		Object response = restTemplate.getForObject("http://localhost:8080/api/doctors/getAllDoctors", Object.class);
		return Arrays.asList(response);
	}

	// Get all appointments
	@GetMapping("/appointments")
	public List<Appointment> getAllAppointments() {
		return appointmentRepository.findAll();
	}

	// Create an Appointment
	@PostMapping("/appointments")
	public ResponseEntity<Appointment> createAppointment(@Valid @RequestBody Appointment appointment) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Appointment> entity = new HttpEntity<>(appointment, headers);
		ResponseEntity<Appointment> response = restTemplate.exchange("http://localhost:8080/api/doctors/{id}",
				HttpMethod.POST, entity, Appointment.class, appointment.getDoctor().getId());
		patientRepository.save(appointment.getPatient());
		appointmentRepository.save(appointment);
		return ResponseEntity.ok(appointment);

	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Appointment> updateAppointmentDate(@Valid @PathVariable(value = "id") String id,@Valid @RequestBody LocalDate date) throws Exception{
		Appointment appointment = appointmentRepository.findById(id).orElseThrow(()->new Exception("The Appointment ID is not found"));
		appointment.setDate(date);
		appointmentRepository.save(appointment);
		return ResponseEntity.ok(appointment);
	}
	
	@DeleteMapping("/appointment/{id}")
	public ResponseEntity<Void> deleteAppointmentByID(@PathVariable(value = "id") String appointmentId) throws Exception {
		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new Exception("Patient not found with id " + appointmentId));

		appointmentRepository.delete(appointment);
		return ResponseEntity.ok().build();
	}
	
}
