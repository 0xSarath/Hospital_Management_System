package com.deloitte.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.deloitte.model.Patient;
import com.deloitte.repo.PatientRepository;
import com.deloitte.service.PatientService;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

	@Mock
	private PatientRepository patientRepository;

	@Mock
	private PatientService patientService;

	@InjectMocks
	private PatientController patientController;

	private Patient patient1, patient2;

	@BeforeEach
	public void setUp() throws Exception {
		patient1 = new Patient("11", "John doe", "Female", 23, "johndoe@gmail.com");
		patient2 = new Patient("12", "John", "Male", 23, "john@gmail.com");
	}

	@Test
	public void testGetAllPatients() {
		List<Patient> patients = new ArrayList<>();
		patients.add(patient1);
		patients.add(patient2);

		when(patientService.getAllPatients()).thenReturn(patients);

		List<Patient> actualPatients = patientController.getAllPatients();

		assertEquals(patients.size(), actualPatients.size());
		assertEquals(patients, actualPatients);
	}

	@Test
	public void testGetPatientById() throws Exception {
		when(patientService.getPatientById(patient1.getId())).thenReturn(patient1);

		ResponseEntity<Patient> response = patientController.getPatientById(patient1.getId());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(patient1.getId(), response.getBody().getId());
		assertEquals(patient1.getName(), response.getBody().getName());
		assertEquals(patient1.getGender(), response.getBody().getGender());
		assertEquals(patient1.getAge(), response.getBody().getAge());
	}

	@Test
	void testCreatePatient() {
		when(patientService.createPatient(patient1)).thenReturn(patient1);

		Patient actualPatient = patientController.createPatient(patient1);

		assertEquals(patient1.getId(), actualPatient.getId());
		assertEquals(patient1.getName(), actualPatient.getName());
		assertEquals(patient1.getGender(), actualPatient.getGender());
		assertEquals(patient1.getAge(), actualPatient.getAge());	}

	@Test
	void testUpdatePatient() throws Exception {
		String patientId = "11";
		Patient updatedPatient = new Patient("11", "John doe", "male", 23, "johndoe@gmail.com");

		when(patientService.updatePatient(patientId, updatedPatient)).thenReturn(updatedPatient);

		ResponseEntity<Patient> response = patientController.updatePatient(patientId, updatedPatient);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedPatient.getId(), response.getBody().getId());
		assertEquals(updatedPatient.getName(), response.getBody().getName());
		assertEquals(updatedPatient.getGender(), response.getBody().getGender());
		assertEquals(updatedPatient.getAge(), response.getBody().getAge());
	}

	@Test
	void testDeletePatient() throws Exception {
		String patientId = "11";
		when(patientService.deletePatient(patientId)).thenReturn("Patient deleted successfully");

		ResponseEntity<String> response = patientController.deletePatient(patientId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Patient deleted successfully", response.getBody());
	}

}
