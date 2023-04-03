package com.deloitte.controller;

import java.util.List;

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

import com.deloitte.model.Patient;
import com.deloitte.service.PatientService;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

	@Autowired
	private PatientService patientService;

	
	/*
	 * Get all Patient Details
	 */	
	@GetMapping("/list")
	public List<Patient> getAllPatients() {
		return patientService.getAllPatients();
	}

	
	/*
	 * Get Patient details based on id
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Patient> getPatientById(@PathVariable(value = "id") String patientId) throws Exception {
		Patient patient = patientService.getPatientById(patientId);
		return ResponseEntity.ok().body(patient);
	}
	
	/*
	 * Create a Patient
	 */	
	@PostMapping("/")
	public Patient createPatient(@Valid @RequestBody Patient patient) {
		return patientService.createPatient(patient);
	}
	
	
	/*
	 * Updating Patient Details based on id
	 */	
	@PutMapping("/{id}")
	public ResponseEntity<Patient> updatePatient(@Valid @PathVariable(value = "id") String patientId,
			@Valid @RequestBody Patient patientDetails) throws Exception{
		Patient updatedPatient = patientService.updatePatient(patientId,patientDetails);
		return ResponseEntity.ok(updatedPatient);
	}
	
	/*
	 * Deleting Patient Details based on id
	 */	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePatient(@PathVariable(value = "id") String patientId) throws Exception {
		String patient = patientService.deletePatient(patientId);
		return ResponseEntity.ok(patient);
	}
}
