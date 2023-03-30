package com.deloitte.controller;

import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.exception.AppointmentException;
import com.deloitte.exception.DoctorException;
import com.deloitte.model.Appointment;
import com.deloitte.model.Patient;
import com.deloitte.service.AppointmentService;

@RestController
@RequestMapping("/api")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

	private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

	/*
	 * Getting All Doctor Details
	 */

	@GetMapping("/getAllDoctors")
	public ResponseEntity<Object> getAllDoctors() throws DoctorException {
		try {
			logger.info("Getting doctor details ");
			Object doctors = appointmentService.getAllDoctorDetails();
			logger.info("Doctor Details: {} ", doctors);
			return ResponseEntity.ok(doctors);
		} catch (DoctorException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/*
	 * Getting All Appointments
	 */
	@GetMapping("/appointments")
	public ResponseEntity<?> getAllAppointments() {
		try {
			logger.info("Getting Appointments");
			ResponseEntity<List<Appointment>> appointments = appointmentService.findAllAppointments();
			logger.info("Appointments so far: {}", appointments);
			return ResponseEntity.ok(appointments);
		} catch (AppointmentException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorMessage());
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}

	}

	/*
	 * Getting Appointments by Doctor Id
	 */
	@GetMapping("/")
	@ExceptionHandler(AppointmentException.class)
	public ResponseEntity<?> getAppointmentsByDoctorId(@RequestParam("id") String doctorId) {
		try {
			logger.info(" Getting Appointments by DoctorId: {}", doctorId);
			ResponseEntity<List<Appointment>> appointments = appointmentService.getAppointmentsByDoctor(doctorId);
			logger.info("Appointments By DoctorId: {}", appointments);
			return ResponseEntity.ok(appointments).getBody();
		} catch (AppointmentException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorMessage());
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}

	}

	/*
	 * Creating an Appointment
	 */
	@PostMapping("/appointments")
	public ResponseEntity<?> createAppointment(@Valid @RequestBody Appointment appointment) throws Exception {
		try {
			logger.info("Appointment Creation Started");
			ResponseEntity<Appointment> appointment1 = appointmentService.createAppointment(appointment);
			logger.info("Created appointment Successfully: {}", appointment1);

			return ResponseEntity.ok(appointment1);
		} catch (AppointmentException e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(e.getErrorMessage());
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}

	}

	/*
	 * Updating the Appointments by appointment Id
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateAppointmentDate(@Valid @PathVariable(value = "id") String id,
			@Valid @RequestBody LocalDate date) throws Exception {
		try {
			logger.info("Updating Appointment Details");
			ResponseEntity<Appointment> appointment = appointmentService.updateAppointmentDate(id, date);
			logger.info("Appointment Details updated Successfully: {}", appointment);
			return ResponseEntity.status(HttpStatus.OK).body(appointment);
		} catch (AppointmentException ex) {
			logger.error("exception caught");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getErrorMessage());
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	/*
	 * Updating the Patient details in the appointment by Appointment Id
	 */
	@PutMapping("/appointment/patient")
	public ResponseEntity<?> updatePatientDetailsByAppointmentID(@RequestParam("id") String appointmentId,
			@Valid @RequestBody Patient patient) throws Exception {
		try {
			logger.info("Updating the patientDetails");
			ResponseEntity<Appointment> appointment = appointmentService
					.updatePatientDetailsByAppointmentID(appointmentId, patient);
			logger.info("Patient details updated Successfully", appointment);
			return ResponseEntity.ok(appointment);
		} catch (AppointmentException ne) {
			logger.error("Appointment Id: {} not found", appointmentId);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ne.getErrorMessage());

		} catch (Exception e) { // TODO: handle exception
			logger.error("Error in Patient Details ");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}

	}

	/*
	 * Deleting an Appointments by appointment Id
	 */
	@DeleteMapping("/appointment/{id}")
	public ResponseEntity<String> deleteAppointmentByID(@Valid @PathVariable(value = "id") String appointmentId)
			throws Exception {
		try {
			logger.info("Deleting the Appointment with id : {}", appointmentId);
			ResponseEntity<String> appointment = appointmentService.deleteAppointmentByID(appointmentId);
			logger.info("Appointment Deleted Successfully");
			return ResponseEntity.ok(appointment).getBody();
		} catch (AppointmentException e) {
			// TODO: handle exception
			logger.error("Appointment Id is not found");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorMessage());
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
