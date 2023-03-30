package com.deloitte.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.deloitte.exception.AppointmentException;
import com.deloitte.exception.DoctorException;
import com.deloitte.model.Appointment;
import com.deloitte.model.Patient;
import com.deloitte.repo.AppointmentRepo;
import com.deloitte.repo.PatientRepository;

@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
	private AppointmentRepo appointmentRepository;
	@Autowired
	private PatientRepository patientRepository;
	@Autowired
	private RestTemplate restTemplate;
	private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);
	
	
	@Override
	public ResponseEntity<List<Appointment>> findAllAppointments() {
		List<Appointment> appointments = appointmentRepository.findAll();
		if (appointments.isEmpty())
			throw new AppointmentException(HttpStatus.NOT_FOUND.value(), "no records found");
		return ResponseEntity.ok(appointments);
	}

	@Override
	public ResponseEntity<Appointment> createAppointment(Appointment appointment) {
		try {
			if (appointmentRepository.findById(appointment.getId()).isPresent()) {
				throw new AppointmentException(HttpStatus.ALREADY_REPORTED.value(),
						"Already the appointment id is mentioned");
			}
			patientRepository.save(appointment.getPatient());
			appointmentRepository.save(appointment);
			return ResponseEntity.ok(appointment);
		}

		catch (Exception e) {
			throw new AppointmentException(HttpStatus.NO_CONTENT.value(), "Missed Some fields" + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<List<Appointment>> getAppointmentsByDoctor(String doctorId) {
		List<Appointment> appointments = appointmentRepository.findAll();
		List<Appointment> appointmentsForDoctor = new ArrayList<>();
		for (Appointment app : appointments) {
			if (app.getDoctor().getId().equals(doctorId)) {
				appointmentsForDoctor.add(app);
			}
		}
		if (appointmentsForDoctor.size() <= 0)
			throw new AppointmentException(HttpStatus.NOT_FOUND.value(),
					"Appointment not found for doctor id:" + doctorId);
		return ResponseEntity.ok(appointmentsForDoctor);

	}

	@Override
	public Object getAllDoctorDetails() throws DoctorException {
		try {
			Object response = restTemplate.exchange("http://localhost:8080/api/doctors/getAllDoctors", HttpMethod.GET,
					null, new ParameterizedTypeReference<Object>() {
					}).getBody();
			if (Arrays.asList(response).get(0).toString().length()==0)
				throw new DoctorException(HttpStatus.NOT_FOUND.value(), "No Doctors Found");
			return response;
		}
		catch(DoctorException de) {
				throw new DoctorException(HttpStatus.NOT_FOUND.value(), "No Doctors Found");
		}
		catch (Exception e) {
			throw new DoctorException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Doctor Service  is down");
		}
	}

	@Override
	public ResponseEntity<Appointment> updateAppointmentDate(String id, LocalDate date) throws AppointmentException {
		try {
			Appointment appointment = appointmentRepository.findById(id).get();
			appointment.setDate(date);
			appointmentRepository.save(appointment);
			return ResponseEntity.ok(appointment);
		} catch (NoSuchElementException ne) {
			throw new AppointmentException(HttpStatus.NOT_FOUND.value(),
					"Appointment id does not exist " + ne.getMessage());
		}

	}

	@Override
	public ResponseEntity<Appointment> updatePatientDetailsByAppointmentID(String appointmentId, Patient patient)
			throws AppointmentException {
		try {
			Appointment appointment = appointmentRepository.findById(appointmentId).get();
			logger.info(appointmentId);
			appointment.setPatient(patient);
			appointmentRepository.save(appointment);
			patientRepository.save(patient);
			return ResponseEntity.ok(appointment);
		} catch (NoSuchElementException ne) {
			throw new AppointmentException(HttpStatus.NOT_FOUND.value(),
					"Appointment id does not exist " + ne.getMessage());
		}

		catch (Exception e) {
			throw new AppointmentException(HttpStatus.NO_CONTENT.value(), "Missed Some fields" + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<String> deleteAppointmentByID(String appointmentId) throws AppointmentException {
		try {
			Appointment appointment = appointmentRepository.findById(appointmentId).get();
			appointmentRepository.delete(appointment);
			return ResponseEntity.ok("Successfully Deleted");
		} catch (NoSuchElementException ne) {
			throw new AppointmentException(HttpStatus.NOT_FOUND.value(),
					"Appointment id does not exist " + ne.getMessage());
		}
	}
}
