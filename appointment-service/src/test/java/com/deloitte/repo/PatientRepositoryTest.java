package com.deloitte.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.deloitte.model.Patient;

@DataMongoTest
@ExtendWith(MockitoExtension.class)
class PatientRepositoryTest {
	@Mock
	private MongoTemplate mongoTemplate;

	@Mock
	private PatientRepository patientRepository;

	private Patient patient, patient1;

	@BeforeEach
	public void setUp() throws Exception {
		patient = new Patient();
		patient.setAge(23);
		patient.setEmail("kaviya@gmail.com");
		patient.setGender("Female");
		patient.setId("111");
		patient.setName("Kaviya");
		patient1 = new Patient("11", "John doe", "Female", 23, "johndoe@gmail.com");
	}

	@Test
	public void testSavePatient() {
		
		when(patientRepository.save(patient)).thenReturn(patient);
		Patient savedPatient = patientRepository.save(patient);
		assertEquals(patient.getName(), savedPatient.getName());
		assertEquals(patient.getAge(), savedPatient.getAge());
		assertEquals(patient.getGender(), savedPatient.getGender());
		assertEquals(patient.getId(), savedPatient.getId());
		assertEquals(patient.getEmail(), savedPatient.getEmail());

	}

	@Test
	public void testGetPatientById() {
	
		when(patientRepository.findById("111")).thenReturn(Optional.of(patient));
		Optional<Patient> result = patientRepository.findById(patient.getId());
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(patient.getId(), result.get().getId());

	}

	@Test
	public void testGetAllPatients() {
		List<Patient> patients = new ArrayList<>();
		patients.add(patient1);
		patients.add(patient);
		when(patientRepository.findAll()).thenReturn(patients);

		List<Patient> actualPatients = patientRepository.findAll();

		assertEquals(patients, actualPatients);
	}

	@Test
	public void testDeleteById() {
		doNothing().when(patientRepository).deleteById(patient.getId());
		patientRepository.deleteById("111");
		verify(patientRepository, times(1)).deleteById(patient.getId());
	}

}
