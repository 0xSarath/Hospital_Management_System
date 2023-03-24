package com.example.ms.service;

import java.util.List;

import com.example.ms.model.Appointment;

public interface AppointmentService {
	List<Appointment> getAppointmentsByDoctor(String doctorId);
}
