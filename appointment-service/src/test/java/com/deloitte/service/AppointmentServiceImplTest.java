package com.deloitte.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.deloitte.exception.AppointmentException;
import com.deloitte.exception.DoctorException;
import com.deloitte.model.Appointment;
import com.deloitte.model.Doctor;
import com.deloitte.model.Patient;
import com.deloitte.repo.AppointmentRepo;
import com.deloitte.repo.PatientRepository;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

	@Mock
	private AppointmentRepo appointmentRepository;

	@Mock
	private PatientRepository patientRepository;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private AppointmentServiceImpl appointmentService;

	private Appointment appointment, appointment1;
	private Doctor doctor, doctor1;

	private Patient patient;

	@BeforeEach
	public void setUp() throws Exception {

		doctor = new Doctor("21", "Paramasivam", "paramasivan@gmail.com", "Dentist");
		doctor1 = new Doctor();
		doctor1.setEmail("paramasivan@gmail.com");
		doctor1.setId("21");
		doctor1.setName("Paramasivam");
		doctor1.setSpecialization("Dentist");
		appointment = new Appointment();
		appointment.setPatient(new Patient("123", "John doe", "Female", 23, "johndoe@gmail.com"));
		appointment.setDoctor(doctor);
		appointment.setDate(LocalDate.now());
		appointment.setReason("Tooth Pain");
		appointment.setId("12");

		appointment1 = new Appointment();
		appointment.setPatient(new Patient("124", "John doe2", "Female", 24, "johndoe2@gmail.com"));
		appointment.setDoctor(doctor1);
		appointment.setDate(LocalDate.now());
		appointment.setReason("Tooth Pain");
		appointment.setId("13");

		patient = new Patient("11", "John doe", "Female", 23, "johndoe@gmail.com");

	}

	@Test
	public void testCreateAppointment() {
		Mockito.when(appointmentRepository.save(appointment)).thenReturn(appointment);
		ResponseEntity<Appointment> response = appointmentService.createAppointment(appointment);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(appointment, response.getBody());

	}

	@Test
	public void testCreateAppointment_AlreadyExists() {
		try {
			when(appointmentRepository.findById(appointment.getId())).thenReturn(Optional.of(appointment));
			appointmentService.createAppointment(appointment);
		} catch (AppointmentException e) {
			assertEquals(HttpStatus.ALREADY_REPORTED.value(), e.getErrorCode());
			assertEquals(e.getErrorMessage(), "Already the appointment id is mentioned");
		}
	}

	@Test
	public void testFindAllAppointments() {
		List<Appointment> appointments = new ArrayList<>();
		appointments.add(appointment);
		appointments.add(appointment1);

		when(appointmentRepository.findAll()).thenReturn(appointments);

		List<Appointment> response = appointmentService.findAllAppointments();
		assertEquals(appointments.size(), response.size());
		assertEquals(appointments, response);
	}

	@Test
	public void testFindAllAppointments_NoAppointmentsFound() {
		List<Appointment> appointments = new ArrayList<>();
		when(appointmentRepository.findAll()).thenReturn(appointments);

		AppointmentException exception = assertThrows(AppointmentException.class,
				() -> appointmentService.findAllAppointments());
		assertEquals(exception.getErrorCode(), HttpStatus.NOT_FOUND.value());
		assertEquals(exception.getErrorMessage(), "No Appointments Found");
	}


	@Test
    public void testGetAllDoctorDetails_DoctorServiceDown() {
        when(restTemplate.getForObject(anyString(), any(), any(), any(ParameterizedTypeReference.class))).thenReturn(new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK));
        DoctorException exception = assertThrows(DoctorException.class, () -> {
			appointmentService.getAllDoctorDetails();
		});
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getErrorCode());
        }

	@Test
	public void testFindByReason() {
		String reason = "Tooth Pain";
		List<Appointment> expectedAppointments = new ArrayList<>();
		expectedAppointments.add(appointment);
		expectedAppointments.add(appointment1);
		when(appointmentRepository.findByReason(reason)).thenReturn(expectedAppointments);
		List<Appointment> actualAppointments = appointmentService.getAppointmentsByReason(reason);
		// verify(appointmentRepository).findByReason(reason);
		assertEquals(expectedAppointments, actualAppointments);
	}

	@Test
	public void testFindByReason_NoAppointmentsFound() {
		String reason = "Surgery";
		when(appointmentRepository.findByReason(reason)).thenReturn(new ArrayList<Appointment>());

		AppointmentException exception = assertThrows(AppointmentException.class, () -> {
			appointmentService.getAppointmentsByReason(reason);
		});
		assertEquals("No appointments Found", exception.getErrorMessage());
	}

	@Test
	public void testUpdateAppointment() {

		when(appointmentRepository.findById(appointment.getId())).thenReturn(Optional.of(appointment));
		when(appointmentRepository.save(appointment)).thenReturn(appointment);
		ResponseEntity<Appointment> updatedAppointment = appointmentService.updateAppointmentDate(appointment.getId(),
				appointment.getDate());
		Assertions.assertEquals(HttpStatus.OK, updatedAppointment.getStatusCode());
		verify(appointmentRepository).save(appointment);
	}

	@Test
	public void testUpdateAppointment_IdNotFound() {
		String id = "3433";
		AppointmentException exception = assertThrows(AppointmentException.class, () -> {
			appointmentService.updateAppointmentDate(id, LocalDate.now());
		});
		assertEquals(HttpStatus.NOT_FOUND.value(), exception.getErrorCode());
		assertEquals("Appointment id does not exist No value present", exception.getErrorMessage());
	}

	@Test
	public void updatePatientDetailsByAppointmentID() {
		when(appointmentRepository.findById(appointment.getId())).thenReturn(Optional.of(appointment));
		when(appointmentRepository.save(appointment)).thenReturn(appointment);
		ResponseEntity<Appointment> updatedAppointment = appointmentService
				.updatePatientDetailsByAppointmentID(appointment.getId(), patient);
		Assertions.assertEquals(HttpStatus.OK, updatedAppointment.getStatusCode());
		verify(appointmentRepository).save(appointment);
	}

	@Test
	public void updatePatientDetailsWhenAppointmentIDNotFound() throws Exception {
		String id = "3433";
		AppointmentException exception = assertThrows(AppointmentException.class, () -> {
			appointmentService.updatePatientDetailsByAppointmentID(id, patient);
		});
		assertEquals(HttpStatus.NOT_FOUND.value(), exception.getErrorCode());
		assertEquals("Appointment id does not exist", exception.getErrorMessage());
	}

	@Test
	public void testDeleteAppointment() {
	
		when(appointmentRepository.findById("12")).thenReturn(Optional.of(appointment));
		ResponseEntity<String> response = appointmentService.deleteAppointmentByID("12");
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Successfully Deleted", response.getBody());
	}

	@Test
	public void testDeleteAppointmentWhenIdNotFOund() throws Exception {
		String id = "13984nkcas";
		AppointmentException exception = assertThrows(AppointmentException.class, () -> {
			appointmentService.deleteAppointmentByID(id);
		});
		assertEquals(HttpStatus.NOT_FOUND.value(), exception.getErrorCode());
		assertEquals("Appointment id does not exist No value present", exception.getErrorMessage());
	}
}
