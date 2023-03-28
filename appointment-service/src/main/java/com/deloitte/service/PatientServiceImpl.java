package com.deloitte.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import com.deloitte.model.Patient;
import com.deloitte.repo.PatientRepository;

@Service
public class PatientServiceImpl implements PatientService{
	@Autowired
	private PatientRepository patientRepository;

	
	/*
	 * Get all Patient Details
	 */	
	@Override
	public List<Patient> getAllPatients() {
		return patientRepository.findAll();
	}

	
	/*
	 * Get Patient details based on id
	 */
	@Override
	public Patient getPatientById(@PathVariable(value = "id") String patientId) throws Exception {
		Patient patient = patientRepository.findById(patientId)
				.orElseThrow(() -> new Exception("Patient not found with id " + patientId));
		return patient;
	}
	
	/*
	 * Create a Patient
	 */	
	@Override
	public Patient createPatient(Patient patient) {
		return patientRepository.save(patient);
	}
	
	
	/*
	 * Updating Patient Details based on id
	 */	
	@Override
	@PutMapping("/{id}")
	public Patient updatePatient(String patientId, Patient patientDetails) throws Exception {
		Patient patient = patientRepository.findById(patientId)
				.orElseThrow(() -> new Exception("Patient not found with id " + patientId));
		patient.setId(patientId);
		patient.setName(patientDetails.getName());
		patient.setGender(patientDetails.getGender());
		patient.setAge(patientDetails.getAge());
		patient.setEmail(patientDetails.getEmail());
		Patient updatedPatient = patientRepository.save(patient);
		return updatedPatient;
	}
	
	/*
	 * Deleting Patient Details based on id
	 */	
	@Override
	public String deletePatient(String patientId) throws Exception {
		Patient patient = patientRepository.findById(patientId)
				.orElseThrow(() -> new Exception("Patient not found with id " + patientId));
		patientRepository.delete(patient);
		return "Patient Successfully Deleted";
	}
}
