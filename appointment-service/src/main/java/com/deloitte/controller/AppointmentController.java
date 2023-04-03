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
import com.deloitte.model.Doctor;
import com.deloitte.model.Patient;
import com.deloitte.service.AppointmentService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api")
@Api(value = "Appointment Controller")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;

	private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

	/*
	 * Getting All Doctor Details 
	 * Request: Get Mapping
	 * Response: List of Doctors
	 */
	@ApiOperation(value = "Get all doctor details", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved the list of doctors"),
			@ApiResponse(code = 404, message = "The doctors list is empty") })
	@GetMapping("/getAllDoctors")
	public ResponseEntity<?> getAllDoctors() throws DoctorException {
		try {
			logger.info("Getting doctor details ");
			List<Doctor> doctors = appointmentService.getAllDoctorDetails();
			logger.info("Doctor Details: {} ", doctors);
			return ResponseEntity.ok(doctors);
		} catch (DoctorException e) {
			logger.error(e.getErrorMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorMessage());
		}
	}
	
	
	/*
	 * Getting All Appointments 
	 * Request: Get Mapping
	 * Response: List of Appointments
	 */
	@ApiOperation(value = "Get all appointments by reason", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved the list of appointments"),
			@ApiResponse(code = 404, message = "No appointments found") })
	@GetMapping("/getAppointmentsByReason")
	public ResponseEntity<?> getByReason(@RequestParam(required = true) String reason) throws AppointmentException {
		try {
			logger.info("Getting Appointments by Reason ");
			List<Appointment>appointments = appointmentService.findByReason(reason);
			logger.info("Appointments fetches", appointments);
			return ResponseEntity.ok(appointments);
		} catch (AppointmentException e) {
			logger.error(e.getErrorMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorMessage());
		}
	}
	
	
	/*
	 * Getting All Appointments 
	 * Request: Get Mapping
	 * Response: List of Appointments
	 */
	@ApiOperation(value = "Get all appointments", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved the list of appointments"),
			@ApiResponse(code = 404, message = "No appointments found") })
	@GetMapping("/appointments")
	public ResponseEntity<?> getAllAppointments() {
		try {
			logger.info("Getting Appointments");
			List<Appointment> appointments = appointmentService.findAllAppointments();
			logger.info("Appointments so far: {}", appointments);
			return ResponseEntity.ok(appointments);
		} catch (AppointmentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorMessage());
		}
	}

	/*
	 * Creating an Appointment
	 * Request: Post Mapping
	 * Response: Appointment
	 */
	@ApiOperation(value = "Create an appointment", response = Appointment.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Appointment created"),
			@ApiResponse(code = 409, message = "Appointment already exists"),
			@ApiResponse(code = 500, message = "Server error") })
	@PostMapping("/appointments")
	public ResponseEntity<?> createAppointment(@Valid @RequestBody Appointment appointment) throws Exception {
		try {
			logger.info("Appointment Creation Started");
			ResponseEntity<Appointment> appointment1 = appointmentService.createAppointment(appointment);
			logger.info("Created appointment Successfully: {}", appointment1);

			return ResponseEntity.ok(appointment1);
		} catch (AppointmentException e) {
			logger.error(e.getErrorMessage());
			return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(e.getErrorMessage());
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}

	}

	/*
	 * Updating the Appointments by appointment Id
	 * Request: Put Mapping
	 * Response: Appointment
	 */
	@ApiOperation(value = "Update an appointment by Appointment Id", response = Appointment.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Appointment Details updated Successfully"),
			@ApiResponse(code = 404, message = "Appointment not found"),
			@ApiResponse(code = 500, message = "Server error") })
	@PutMapping("/{id}")
	public ResponseEntity<?> updateAppointmentDate(@Valid @PathVariable(value = "id") String id,
			@Valid @RequestBody LocalDate date, @RequestParam(required = false) String reason) throws Exception {
		try {
			logger.info("Updating Appointment Details");
			ResponseEntity<Appointment> appointment = appointmentService.updateAppointmentDate(id, date);
			logger.info("Appointment Details updated Successfully: {}", appointment);
			return ResponseEntity.status(HttpStatus.OK).body(appointment);
		} catch (AppointmentException ex) {
			logger.error(ex.getErrorMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getErrorMessage());
		}
	}

	/*
	 * Updating the Patient details in the appointment by Appointment Id
	 * Request: Put Mapping
	 * Response: Appointment
	 */
	
	@ApiOperation(value = "Update Patient Details by Appointment Id", response = Appointment.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Patient Details updated Successfully"),
			@ApiResponse(code = 404, message = "Appointment Id not found"),
			@ApiResponse(code = 500, message = "Error in Patient Details") })
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
	 * Request: Delete Mapping
	 * Response: String
	 */
	@ApiOperation(value = "Delete an appointment by Appointment Id", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Appointment Deleted Successfully"),
			@ApiResponse(code = 404, message = "Appointment not found"),
			@ApiResponse(code = 500, message = "Server error") })
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
			logger.error(e.getErrorMessage());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorMessage());
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
