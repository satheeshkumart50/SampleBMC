package com.bmc.appointmentserivce.dao;

import java.util.Date;
import java.util.List;

import com.bmc.appointmentserivce.model.entity.Availability;

/**
 * This interface to perform the CRUD operations on Availability
 *
 */
public interface AvailabilityDAO {

	Availability saveAvailability(Availability availability);

	List<Availability> getAvailabilityByDoctorId(String doctorId);

	Availability getAvailabilityByDocIdAndTime(String doctorId, Date appointmentDate, String timeSlot);

	void deleteAvailabilitybyDoctorIdandAvailDate(String doctorId, Date availDate);

}
