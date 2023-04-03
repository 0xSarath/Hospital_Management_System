package com.deloitte.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.deloitte.exception.AppointmentException;
import com.deloitte.exception.DoctorException;
import com.deloitte.model.Appointment;
import com.deloitte.model.Doctor;
import com.deloitte.model.Patient;
import com.deloitte.repo.AppointmentRepo;
import com.deloitte.service.AppointmentService;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {

	@Mock
	private AppointmentService appointmentService;

	@Mock
	private AppointmentRepo appointmentRepository;

	@InjectMocks
	private AppointmentController appointmentController;

	private Appointment appointment, appointment1;

	private Patient patient;

	@BeforeEach
	public void setUp() throws Exception {
		appointment = new Appointment();
		appointment.setPatient(new Patient("123", "John doe", "Female", 23, "johndoe@gmail.com"));
		appointment.setDoctor(new Doctor("21", "Paramasivam", "paramasivan@gmail.com", "Dentist"));
		appointment.setDate(LocalDate.now());
		appointment.setReason("Tooth Pain");
		appointment.setId("12");

		appointment1 = new Appointment("13",LocalDate.now(),new Doctor("21", "Paramasivam", "paramasivan@gmail.com", "Dentist"),new Patient("124", "John doe2", "Female", 24, "johndoe2@gmail.com"),"Tooth Pain");
	
		patient = new Patient("11", "John doe", "Female", 23, "johndoe@gmail.com");

	}

	@Test
	void testGetAllDoctors() throws DoctorException {
		List<Doctor> doctorList = new ArrayList<>();
		Doctor doctor1 = new Doctor("232", "Arun", "arun@gmail.com", "MBBS");
		doctorList.add(doctor1);
		Mockito.when(appointmentService.getAllDoctorDetails()).thenReturn(doctorList);
		ResponseEntity<?> responseEntity = appointmentController.getAllDoctors();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(doctorList, responseEntity.getBody());
	}

	@Test
	void testGetAllDoctors_NotFound() throws DoctorException {
		DoctorException exception = new DoctorException();
		Mockito.when(appointmentService.getAllDoctorDetails()).thenThrow(exception);
		ResponseEntity<?> responseEntity = appointmentController.getAllDoctors();
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertNull(responseEntity.getBody());
	}

	@Test
	public void testGetByReason() {
		List<Appointment> appointments = new ArrayList<>();
		appointments.add(appointment);
		appointments.add(appointment1);

		Mockito.when(appointmentService.findByReason("Tooth Pain")).thenReturn(appointments);

		ResponseEntity<?> responseEntity = appointmentController.getByReason("Tooth Pain");
		Mockito.verify(appointmentService, Mockito.times(1)).findByReason("Tooth Pain");
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(appointments, responseEntity.getBody());

	}

	@Test
	public void testGetByReason_NotFound() {
		List<Appointment> appointments = new ArrayList<>();
		appointments.add(appointment);
		appointments.add(appointment1);

		AppointmentException exception = new AppointmentException("Appointment not found");
		when(appointmentService.findByReason("ToothPain")).thenThrow(exception);

		ResponseEntity<?> responseEntity = appointmentController.getByReason("ToothPain");
		Mockito.verify(appointmentService, Mockito.times(1)).findByReason("ToothPain");
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	public void testGetAllAppointments() {
		List<Appointment> appointments = new ArrayList<>();
		appointments.add(appointment);
		appointments.add(appointment1);

		Mockito.when(appointmentService.findAllAppointments()).thenReturn(appointments);

		ResponseEntity<?> responseEntity = appointmentController.getAllAppointments();
		Mockito.verify(appointmentService, Mockito.times(1)).findAllAppointments();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(appointments, responseEntity.getBody());

	}

	@Test
	public void testGetAllAppointments_NotFound() {
		AppointmentException exception = new AppointmentException();
		Mockito.when(appointmentService.findAllAppointments()).thenThrow(exception);

		ResponseEntity<?> responseEntity = appointmentController.getAllAppointments();
		Mockito.verify(appointmentService, Mockito.times(1)).findAllAppointments();
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

	}

	@Test
	void testCreateAppointment() throws Exception {
		ResponseEntity<Appointment> expectedResponse = ResponseEntity.ok(appointment);
		when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(expectedResponse);
		ResponseEntity<?> actualResponse = appointmentController.createAppointment(appointment);
		assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
		assertEquals(expectedResponse, actualResponse.getBody());

	}

	@Test
	public void testUpdateAppointmentDate_Success() throws Exception {
		ResponseEntity<Appointment> expectedResponse = ResponseEntity.status(HttpStatus.OK).body(appointment);
		when(appointmentService.updateAppointmentDate(appointment.getId(), appointment.getDate()))
				.thenReturn(expectedResponse);
		ResponseEntity<?> actualResponse = appointmentController.updateAppointmentDate(appointment.getId(),
				appointment.getDate(), appointment.getReason());
		assertEquals(expectedResponse.getStatusCode(), actualResponse.getStatusCode());
		assertEquals(expectedResponse, actualResponse.getBody());
		verify(appointmentService, times(1)).updateAppointmentDate(appointment.getId(), appointment.getDate());
	}

	@Test
	public void testUpdateAppointment_DateNotFoundException() throws Exception {
		String id = "1";
		LocalDate date = LocalDate.of(2023, 4, 1);
		AppointmentException exception = new AppointmentException("Appointment not found");
		when(appointmentService.updateAppointmentDate(id, date)).thenThrow(exception);

		ResponseEntity<?> actualResponse = appointmentController.updateAppointmentDate(id, date, "Tooth pain");

		assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
		assertEquals(exception.getErrorMessage(), actualResponse.getBody());
		verify(appointmentService, times(1)).updateAppointmentDate(id, date);
	}

	@Test
	public void testUpdatePatientDetailsByAppointmentID_Success() throws Exception {
		appointment.setPatient(patient);

		Mockito.when(appointmentService.updatePatientDetailsByAppointmentID(appointment.getId(), patient))
				.thenReturn(ResponseEntity.ok(appointment));

		ResponseEntity<?> response = appointmentController.updatePatientDetailsByAppointmentID(appointment.getId(),
				patient);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}

	@Test
	public void testUpdatePatientDetailsByAppointmentID_NotFound() throws Exception {

		AppointmentException appointmentException = new AppointmentException("Appointment not found");
		Mockito.when(appointmentService.updatePatientDetailsByAppointmentID("600", patient))
				.thenThrow(appointmentException);

		ResponseEntity<?> response = appointmentController.updatePatientDetailsByAppointmentID("600", patient);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertNull(response.getBody());
	}

	@Test
	public void testUpdatePatientDetailsByAppointmentID_InternalServerError() throws Exception {
		Exception exception = new Exception("Internal Server Error");
		Mockito.when(appointmentService.updatePatientDetailsByAppointmentID(appointment.getId(), patient))
				.thenThrow(exception);

		ResponseEntity<?> response = appointmentController.updatePatientDetailsByAppointmentID(appointment.getId(),
				patient);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(String.class, response.getBody().getClass());
		assertEquals("Internal Server Error", response.getBody());
	}

	@Test
	public void testCreateAppointment_AlreadyExists() throws Exception {
		AppointmentException appointmentException = new AppointmentException("Appointment already exists");
		ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
				.body(appointmentException.getErrorMessage());
		when(appointmentService.createAppointment(any(Appointment.class))).thenThrow(appointmentException);
		ResponseEntity<?> actualResponse = appointmentController.createAppointment(appointment);
		assertEquals(HttpStatus.ALREADY_REPORTED, actualResponse.getStatusCode());
		assertEquals(expectedResponse, actualResponse);
	}

	@Test
	public void testCreateAppointment_InternalServerError() throws Exception {
		Exception exception = new Exception("Internal server error");
		ResponseEntity<String> expectedResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(exception.getMessage());
		when(appointmentService.createAppointment(any(Appointment.class))).thenThrow(exception);
		ResponseEntity<?> actualResponse = appointmentController.createAppointment(appointment);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
		assertEquals(expectedResponse, actualResponse);
	}

	@Test
	public void testDeleteAppointmentByID() throws Exception {
		String appointmentId = "12345";
		String expectedMessage = "Appointment deleted successfully";
		ResponseEntity<String> expectedResponseEntity = ResponseEntity.ok(expectedMessage);
		Mockito.when(appointmentService.deleteAppointmentByID(appointmentId)).thenReturn(expectedResponseEntity);

		ResponseEntity<String> actualResponseEntity = appointmentController.deleteAppointmentByID(appointmentId);
		assertEquals(HttpStatus.OK, actualResponseEntity.getStatusCode());
		assertEquals(expectedMessage, actualResponseEntity.getBody());
	}

	@Test
	public void testDeleteAppointmentByID_NotFound() throws Exception {
		String appointmentId = "12345";
		AppointmentException exception = new AppointmentException("Appoinment Not Found");
		Mockito.when(appointmentService.deleteAppointmentByID(appointmentId)).thenThrow(exception);

		ResponseEntity<String> actualResponseEntity = appointmentController.deleteAppointmentByID(appointmentId);
		assertEquals(HttpStatus.NOT_FOUND, actualResponseEntity.getStatusCode());
		assertEquals(exception.getErrorMessage(), actualResponseEntity.getBody());
	}

	@Test
	public void testDeleteAppointmentByID_Internal_Server_Error() throws Exception {
		String appointmentId = "12345";
		Exception exception = new Exception("Internal Server Error");
		Mockito.when(appointmentService.deleteAppointmentByID(appointmentId)).thenThrow(exception);

		ResponseEntity<String> actualResponseEntity = appointmentController.deleteAppointmentByID(appointmentId);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponseEntity.getStatusCode());
		assertEquals(exception.getMessage(), actualResponseEntity.getBody());
	}

}
