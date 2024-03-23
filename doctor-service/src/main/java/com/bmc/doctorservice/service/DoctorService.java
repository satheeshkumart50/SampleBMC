package com.bmc.doctorservice.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.bmc.doctorservice.model.Doctor;

public interface DoctorService {
	
	List<Doctor> getAllDoctors();

	Doctor getDoctorById(String doctorId);

	Doctor saveDoctor(Doctor doctor);

	List<Doctor> getDoctorsByStatus(String Status);

	List<Doctor> getDoctorsBySpeciality(String Speciality);

	List<Doctor> getDoctorsByStatusAndSpeciality(String Status, String Speciality);
	
	void uploadFiles(String doctorId, MultipartFile file);
	
	String[] getFileNames(String key);
}
