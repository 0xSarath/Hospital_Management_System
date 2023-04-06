package com.deloitte.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deloitte.entity.Doctor;
import com.deloitte.repo.DoctorRepo;

@Service
public class DoctorServiceImpl implements DoctorService {
	@Autowired
	private DoctorRepo doctorRepo;

	@Override
	public List<Doctor> findBySpecialization(String specialization) {
		// TODO Auto-generated method stub
		return doctorRepo.findBySpecialization(specialization);
	}
	
}
