package com.deloitte.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.deloitte.exception.AppointmentException;
import com.deloitte.exception.DoctorException;
import com.deloitte.model.Appointment;
import com.deloitte.model.Doctor;
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

	@Value("${my.url}")
	private String serviceUrl;
	@Value("${my.custom.url1}")
	private String serviceUrl1;
	private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

	@Override
	public List<Appointment> findAllAppointments() {
		List<Appointment> appointments = appointmentRepository.findAll();
		if (appointments.isEmpty())
			throw new AppointmentException(HttpStatus.NOT_FOUND.value(), "No Appointments Found");
		return appointments;
	}

	@Override
	public ResponseEntity<Appointment> createAppointment(Appointment appointment) {
		if (appointmentRepository.findById(appointment.getId()).isPresent()) {
			throw new AppointmentException(HttpStatus.ALREADY_REPORTED.value(),
					"Already the appointment id is mentioned");
		}
		String doc_id = appointment.getDoctor().getId();
		Doctor doctor = restTemplate.getForObject(serviceUrl1 + "/" + doc_id, Doctor.class);
		appointment.setDoctor(doctor);
		patientRepository.save(appointment.getPatient());
		appointmentRepository.save(appointment);
		return ResponseEntity.ok(appointment);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Doctor> getAllDoctorDetails() throws DoctorException {
		List<Doctor> response;
		try {
			response = restTemplate.getForObject(serviceUrl, List.class);

		} catch (Exception e) {
			throw new DoctorException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Doctor Service  is down");
		}
		return response;
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
					"Appointment id does not exist " + id+ " not present");
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
			throw new AppointmentException(HttpStatus.NOT_FOUND.value(), "Appointment id "+appointmentId+" does not exist");
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

	@Override
	public List<Appointment> getAppointmentsByReason(String reason) {
		List<Appointment> appointments = appointmentRepository.findByReason(reason);
		if (appointments.isEmpty()) {
			throw new AppointmentException(HttpStatus.NOT_FOUND.value(), "No appointments Found");
		} else {
			return appointments.stream().map(appointment -> {
				appointment.setReason(appointment.getReason());
				return appointment;
			}).collect(Collectors.toList());
		}
	}


}
