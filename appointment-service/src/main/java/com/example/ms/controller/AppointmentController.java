package com.example.ms.controller;

import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
	public Object getAllDoctorDetails() {

		Object response = restTemplate.getForObject("http://localhost:8080/api/doctors/getAllDoctors", Object.class);
		return response;
	}

	// Get all appointments
	@GetMapping("/appointments")
	public List<Appointment> getAllAppointments() {
		return appointmentRepository.findAll();
	}

	// Create an Appointment
	@PostMapping("/appointments")
	public ResponseEntity<Appointment> createAppointment(@Valid @RequestParam("doctorId") String id,@Valid @RequestParam("reason") String reason,@Valid @RequestParam("Date") String date,@Valid @RequestParam("AppointmentId") String appointmentid, @Valid @RequestBody Patient patient) throws Exception {
		Doctor response = restTemplate.exchange("http://localhost:8080/api/doctors/doctorId/{id}",HttpMethod.GET, null,
				new ParameterizedTypeReference<Doctor>() {
				}, id).getBody();
		Appointment appointment = new Appointment();
		appointment.setId(appointmentid);
		appointment.setDoctor(response);
		appointment.setPatient(patient);
		appointment.setReason(reason);
		appointment.setDate(LocalDate.parse(date));
		patientRepository.save(patient);
		appointmentRepository.save(appointment);
		return ResponseEntity.ok(appointment);
	}
	@GetMapping("/")
	public ResponseEntity<List<Appointment>> getAppointmentsByDoctor(@RequestParam("id") String doctorId) {
		List<Appointment> appointments = appointmentRepository.getAppointmentsByDoctor(doctorId);
		return ResponseEntity.ok(appointments);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Appointment> updateAppointmentDate(@Valid @PathVariable(value = "id") String id,
			@Valid @RequestBody LocalDate date) throws Exception {
		Appointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new Exception("The Appointment ID is not found"));
		appointment.setDate(date);
		appointmentRepository.save(appointment);
		return ResponseEntity.ok(appointment);
	}

	@DeleteMapping("/appointment/{id}")
	public ResponseEntity<String> deleteAppointmentByID(@PathVariable(value = "id") String appointmentId)
			throws Exception {
		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new Exception("Patient not found with id " + appointmentId));

		appointmentRepository.delete(appointment);
		return ResponseEntity.ok().body("Successfully Deleted");
	}

}
