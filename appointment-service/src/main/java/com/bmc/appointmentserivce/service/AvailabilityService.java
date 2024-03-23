package com.bmc.appointmentserivce.service;

import java.util.Date;
import java.util.List;

import com.bmc.appointmentserivce.model.entity.Availability;

public interface AvailabilityService {
	
	Availability saveAvailability(Availability availability);
	
	List<Availability> getAvailabilityByDoctorId(String doctorId);

	void deleteAvailabilitybyDoctorIdandAvailDate(String doctorId, Date availDate);

	Availability getAvailabilityByDocIdAndTime(String doctorId, Date appointmentDate, String timeSlot);

}
