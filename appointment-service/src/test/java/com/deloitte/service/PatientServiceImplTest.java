package com.deloitte.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deloitte.model.Patient;
import com.deloitte.repo.PatientRepository;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

	@Mock
	private PatientRepository patientRepository;

	@InjectMocks
	private PatientServiceImpl patientServiceImpl;

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

		when(patientRepository.findAll()).thenReturn(patients);

		List<Patient> result = patientServiceImpl.getAllPatients();

		assertEquals(patients.size(), result.size());
	}

	@Test
    public void testGetPatientById() throws Exception {

        when(patientRepository.findById(patient1.getId())).thenReturn(Optional.of(patient1));

        Patient result = patientServiceImpl.getPatientById(patient1.getId());

        assertEquals(patient1.getName(), result.getName());
    }

	@Test
	void testCreatePatient() {
		when(patientRepository.save(patient1)).thenReturn(patient1);

        Patient result = patientServiceImpl.createPatient(patient1);

        assertEquals(patient1.getName(), result.getName());	}

	@Test
	void testUpdatePatient() throws Exception {
		Patient updatedPatient = new Patient("11", "Alice Doe", "Female", 25, "alice.doe@test.com");
		when(patientRepository.findById(patient1.getId())).thenReturn(Optional.of(patient1));
		when(patientRepository.save(patient1)).thenReturn(updatedPatient);

		Patient result = patientServiceImpl.updatePatient(patient1.getId(), updatedPatient);

		assertEquals(updatedPatient.getName(), result.getName());
		assertEquals(patient1.getId(), result.getId());
	}

	@Test
	void testDeletePatient() throws Exception {
		String id = "11";
		when(patientRepository.findById(id)).thenReturn(Optional.of(patient1));
		String result = patientServiceImpl.deletePatient(id);

		assertEquals("Patient Successfully Deleted", result);
	}
}

