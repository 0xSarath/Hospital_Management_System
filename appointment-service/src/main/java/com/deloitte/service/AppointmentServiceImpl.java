package com.deloitte.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

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
	
	@Override
	public List<Appointment> findAllAppointments() {
		List<Appointment> appointments = appointmentRepository.findAll();
		return appointments;
	}
	
	@Override
	public List<Appointment> getAppointmentsByDoctor(String doctorId) {
		List<Appointment> appointments = appointmentRepository.findAll();
		
		List<Appointment> appointmentsForDoctor = new ArrayList<>();
		for (Appointment app : appointments) {
			if (app.getDoctor().getId().equals(doctorId)) {
				appointmentsForDoctor.add(app);
			}
		}
		return appointmentsForDoctor;

	}

	@Override
	public Object getAllDoctorDetails() {
		Object response = restTemplate.getForObject("http://localhost:8080/api/doctors/getAllDoctors", Object.class);
		return response;
	}

	@Override
	public String deleteAppointmentByID(@PathVariable(value = "id") String appointmentId) throws Exception {
		Appointment appointment = appointmentRepository.findById(appointmentId)
				.orElseThrow(() -> new Exception("Patient not found with id " + appointmentId));
		appointmentRepository.delete(appointment);
		return "Successfully Deleted";
	}

	@Override
	public Appointment createAppointment(@RequestBody Appointment appointment) throws Exception {
		
		if(appointmentRepository.findById(appointment.getId()).isPresent()) {
			throw new Exception("Already the appointment id is mentioned");

		}
		patientRepository.save(appointment.getPatient());
		appointmentRepository.save(appointment);
		return appointment;
	}
	@Override
	public Appointment updateAppointmentDate(@PathVariable(value = "id") String id,
			@Valid @RequestBody LocalDate date) throws Exception {
		Appointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new Exception("The Appointment ID is not found"));
		appointment.setDate(date);
		appointmentRepository.save(appointment);
		return appointment;
	}

	@Override
	public Appointment updatePatientDetailsByAppointmentID(String appointmentId, Patient patient) throws Exception {
		// TODO Auto-generated method stub
		Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow(()-> new Exception("Appointment Id: " +appointmentId +" not found"));
		appointment.setPatient(patient);
		appointmentRepository.save(appointment);
		patientRepository.save(patient);
		return appointment ;
	}
	
}
