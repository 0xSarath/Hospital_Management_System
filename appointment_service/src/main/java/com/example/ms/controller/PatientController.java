package com.example.ms.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms.model.Patient;
import com.example.ms.repo.PatientRepository;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

	@ApiIgnore
	@RequestMapping(value = "/")
	public void redirect(HttpServletResponse response) throws IOException {
		response.sendRedirect("/swagger-ui.html");
	}

	@Autowired // auto inject bean
	private PatientRepository patientRepository;

	@GetMapping("/")
	public List<Patient> getAllPatients() {
		return patientRepository.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Patient> getPatientById(@PathVariable(value = "id") String patientId) throws Exception {
		Patient patient = patientRepository.findById(patientId)
				.orElseThrow(() -> new Exception("Patient not found with id " + patientId));
		return ResponseEntity.ok().body(patient);
	}

	@PostMapping("/")
	public Patient createPatient(@Validated @RequestBody Patient patient) {
		return patientRepository.save(patient);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Patient> updatePatient(@PathVariable(value = "id") String patientId,
			@Validated @RequestBody Patient patientDetails) throws Exception {
		Patient patient = patientRepository.findById(patientId)
				.orElseThrow(() -> new Exception("Patient not found with id " + patientId));

		patient.setName(patientDetails.getName());
		patient.setGender(patientDetails.getGender());
		patient.setAge(patientDetails.getAge());

		Patient updatedPatient = patientRepository.save(patient);
		return ResponseEntity.ok(updatedPatient);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePatient(@PathVariable(value = "id") String patientId) throws Exception {
		Patient patient = patientRepository.findById(patientId)
				.orElseThrow(() -> new Exception("Patient not found with id " + patientId));

		patientRepository.delete(patient);

		return ResponseEntity.ok().build();
	}
}
