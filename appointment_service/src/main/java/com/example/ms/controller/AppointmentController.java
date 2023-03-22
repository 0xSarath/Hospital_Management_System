package com.example.ms.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.ms.entity.Doctor;
import com.deloitte.ms.repo.DoctorRepo;
import com.example.ms.model.Appointment;
import com.example.ms.model.Patient;
import com.example.ms.repo.AppointmentRepo;
import com.example.ms.repo.PatientRepository;


@RestController
@RequestMapping("/api")
public class AppointmentController {
    
    @Autowired
    private AppointmentRepo appointmentRepository;
    
    @Autowired
    private DoctorRepo doctorRepository;
    
    @Autowired
    private PatientRepository patientRepository;
    
    
    //Create an Appointment
    @PostMapping("/appointments")
    public Appointment createAppointment(@Valid @RequestBody Appointment appointment) throws Exception {
        Optional<Doctor> doctor = doctorRepository.findById(appointment.getDoctor().getId());
        if (!doctor.isPresent()) {
            throw new Exception("Doctor not found with id " + appointment.getDoctor().getId());
        }
        
        Optional<Patient> patient = patientRepository.findById(appointment.getPatient().getId());
        if (!patient.isPresent()) {
            throw new Exception("Patient not found with id " + appointment.getPatient().getId());
        }
        return appointmentRepository.save(appointment);
    }   
    
    
    // Get all appointments
    @GetMapping("/appointments")
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

}

