package com.deloitte.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deloitte.entity.Doctor;

@Service
public interface DoctorService {
	List<Doctor> findBySpecialization(String specialization);
}
