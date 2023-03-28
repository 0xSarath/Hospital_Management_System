package com.deloitte.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deloitte.model.Patient;

@Service
public interface PatientService {

	List<Patient> getAllPatients();

	Patient getPatientById(String patientId) throws Exception;

	Patient createPatient(Patient patient);

	Patient updatePatient(String patientId, Patient patientDetails) throws Exception;

	String deletePatient(String patientId) throws Exception;

}
