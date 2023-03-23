package com.example.ms.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

@ApiIgnore
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

	
	
	//Get all Patient Details
	@GetMapping("/")
	public List<Patient> getAllPatients() {
		return patientRepository.findAll();
	}

	
	//Get Patient details based on id
	@GetMapping("/{id}")
	public ResponseEntity<Patient> getPatientById(@PathVariable(value = "id") String patientId) throws Exception {
		Patient patient = patientRepository.findById(patientId)
				.orElseThrow(() -> new Exception("Patient not found with id " + patientId));
		return ResponseEntity.ok().body(patient);
	}

	
	//Create a Patient
	@PostMapping("/")
	public Patient createPatient(@Valid @RequestBody Patient patient) {
		return patientRepository.save(patient);
	}
	
	
	//Updating Patient Details based on id
	@PutMapping("/{id}")
	public ResponseEntity<Patient> updatePatient(@PathVariable(value = "id") String patientId,
			@Valid @RequestBody Patient patientDetails) throws Exception {
		Patient patient = patientRepository.findById(patientId)
				.orElseThrow(() -> new Exception("Patient not found with id " + patientId));
		patient.setId(patientId);
		patient.setName(patientDetails.getName());
		patient.setGender(patientDetails.getGender());
		patient.setAge(patientDetails.getAge());
		patient.setEmail(patientDetails.getEmail());
		Patient updatedPatient = patientRepository.save(patient);
		return ResponseEntity.ok(updatedPatient);
	}
	
	
	//Deleting Patient Details based on id
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePatient(@PathVariable(value = "id") String patientId) throws Exception {
		Patient patient = patientRepository.findById(patientId)
				.orElseThrow(() -> new Exception("Patient not found with id " + patientId));

		patientRepository.delete(patient);

		return ResponseEntity.ok().build();
	}
}
