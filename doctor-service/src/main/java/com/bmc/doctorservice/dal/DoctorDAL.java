package com.bmc.doctorservice.dal;

import java.util.List;

import com.bmc.doctorservice.model.Doctor;

/**
 * This interface to perform the CRUD operations on Doctor Object in Mongo DB
 *
 */
public interface DoctorDAL {
	
	List<Doctor> getAllDoctors();

	Doctor getDoctorById(String doctorId);

	Doctor saveDoctor(Doctor doctor);

	List<Doctor> getDoctorsByStatus(String status);

	List<Doctor> getDoctorsBySpeciality(String speciality);

	List<Doctor> getDoctorsByStatusAndSpeciality(String status, String speciality);

}
