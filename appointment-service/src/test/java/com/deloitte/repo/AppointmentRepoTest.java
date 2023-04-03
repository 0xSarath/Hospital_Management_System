package com.deloitte.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.deloitte.model.Appointment;
import com.deloitte.model.Doctor;
import com.deloitte.model.Patient;

@DataMongoTest
@ExtendWith(MockitoExtension.class)
class AppointmentRepoTest {

	@Mock
	private MongoTemplate mongoTemplate;

	@Mock
	private AppointmentRepo appointmentRepository;
	private Appointment appointment, appointment1;

	@BeforeEach
	public void setUp() throws Exception {
		appointment = new Appointment();
		appointment.setPatient(new Patient("123", "John doe", "Female", 23, "johndoe@gmail.com"));
		appointment.setDoctor(new Doctor("21", "Paramasivam", "paramasivan@gmail.com", "Dentist"));
		appointment.setDate(LocalDate.now());
		appointment.setReason("Tooth Pain");
		appointment.setId("12");

		appointment1 = new Appointment();
		appointment.setPatient(new Patient("124", "John doe2", "Female", 24, "johndoe2@gmail.com"));
		appointment.setDoctor(new Doctor("21", "Paramasivam", "paramasivan@gmail.com", "Dentist"));
		appointment.setDate(LocalDate.now());
		appointment.setReason("Tooth Pain");
		appointment.setId("13");

	}

	@Test
	public void testSaveAppointment() {

		when(appointmentRepository.save(appointment)).thenReturn(appointment);
		Appointment savedAppointment = appointmentRepository.save(appointment);
		assertNotNull(savedAppointment.getId());
		assertEquals(appointment.getDoctor(), savedAppointment.getDoctor());
		assertEquals(appointment.getPatient(), savedAppointment.getPatient());
		assertEquals(appointment.getDate(), savedAppointment.getDate());
		assertEquals(appointment.getReason(), savedAppointment.getReason());
	}

	@Test
	public void testGetAppointmentById() {	
				when(appointmentRepository.findById(appointment.getId())).thenReturn(Optional.of(appointment));
		Optional<Appointment> result = appointmentRepository.findById(appointment.getId());
		Assertions.assertTrue(result.isPresent());
		Assertions.assertEquals(appointment, result.get());
	}

	@Test
	public void testGetAllAppointments() {
		List<Appointment> appointments = new ArrayList<>();

		appointments.add(appointment);
		appointments.add(appointment1);
		when(appointmentRepository.findAll()).thenReturn(appointments);
		List<Appointment> actualAppointments = appointmentRepository.findAll();

		assertEquals(appointments, actualAppointments);
	}

	@Test
	public void testDeleteById() {

		String id = "12";
		doNothing().when(appointmentRepository).deleteById(id);
		appointmentRepository.deleteById(id);
		verify(appointmentRepository, times(1)).deleteById(id);
	}

}
