package com.example.ms.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ms.model.Appointment;
import com.example.ms.repo.AppointmentRepo;

@Service
public class AppointmentServiceImpl implements AppointmentService {
	@Autowired
	private AppointmentRepo appointmentRepository;

	@Override
	public List<Appointment> getAppointmentsByDoctor(String doctorId) {
		List<Appointment> appointments = appointmentRepository.findAll();
		List<Appointment> appointmentsForDoctor= new ArrayList<>();
		
		for(Appointment app: appointments) {
			if (app.getDoctor().getId()==doctorId) {
				appointmentsForDoctor.add(app);
			}
			
		}
        return appointmentsForDoctor;
       
	}
	
	
}
