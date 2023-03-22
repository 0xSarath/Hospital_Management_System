package com.deloitte.ms.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

//import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.ms.entity.Doctor;
import com.deloitte.ms.repo.DoctorRepo;

import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

	@ApiIgnore
	@RequestMapping(value = "/")
	public void redirect(HttpServletResponse response) throws IOException {
		response.sendRedirect("/swagger-ui.html");
	}

	@Autowired
	private DoctorRepo doctorRepository;

	@GetMapping("/getAllDoctors")
	public List<Doctor> getAllDoctors() {
		return doctorRepository.findAll();
	}

	@GetMapping("/{specialization}")
	public List<Doctor> findDoctorsBySpecialization(@PathVariable(value = "specialization") String specialization) {
		return doctorRepository.findBySpecialization(specialization);
	}

	@PostMapping("/addDoctors")
	public Doctor addDoctors(@Valid @RequestBody Doctor doctor) {
		return doctorRepository.save(doctor);
	}

	@DeleteMapping("/{id}")
	public void deleteDoctorById(@PathVariable(value = "id") String id) {
		doctorRepository.deleteById(id);
	}
}
