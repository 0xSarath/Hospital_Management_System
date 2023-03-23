package com.deloitte.ms.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

//import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@GetMapping("/doctorId/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable(value = "id") String doctorId)
            throws Exception {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new Exception("Doctor not found with id " + doctorId));
        System.out.println(doctor);
        return ResponseEntity.ok(doctor);
    }
	
	
	@GetMapping("/{specialization}")
	public List<Doctor> findDoctorsBySpecialization(@PathVariable(value = "specialization") String specialization) {
		return doctorRepository.findBySpecialization(specialization);
	}

	@PostMapping("/addDoctors")
	public Doctor addDoctors(@Valid @RequestBody Doctor doctor) {
		return doctorRepository.save(doctor);
	}
	
	
	@PostMapping("/{id}")
	public Doctor addAppointmentsByDoctorId(@Valid @PathVariable String id) throws Exception {
		Doctor doctor2 = doctorRepository.findById(id).orElseThrow(()->new Exception("Doctor not found with doctorId: "+id));
		return doctorRepository.save(doctor2);
	}
	
	//Update doctor details by doctorid
	@PutMapping("/{id}")
	public ResponseEntity<Doctor> updateById(@PathVariable(value = "id") String id ,@Valid @RequestBody() Doctor doctor) throws Exception{
		Doctor doctor1 =  doctorRepository.findById(id).orElseThrow(()->new Exception("Doctor not found with doctorId: "+id));
		doctor1.setName(doctor.getName());
		doctor1.setEmail(doctor.getEmail());
		doctor1.setSpecialization(doctor.getSpecialization());
		doctor1.setId(id);
		
		Doctor updatedDoctor = doctorRepository.save(doctor); 
		return ResponseEntity.ok().body(updatedDoctor);
	}
	
	
	@DeleteMapping("/{id}")
	public void deleteDoctorById(@PathVariable(value = "id") String id) {
		doctorRepository.deleteById(id);
	}
}
