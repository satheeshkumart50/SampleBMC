package com.bmc.appointmentserivce.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmc.appointmentserivce.dao.AvailabilityDAO;
import com.bmc.appointmentserivce.model.entity.Availability;

/**
 * This Service class reaches out to DAO layer to perform CRUD operation on appointment object
 *
 */
@Service
public class AvailabilityServiceImpl implements AvailabilityService {

	private AvailabilityDAO availabilityDAO;

	@Autowired
	public AvailabilityServiceImpl(AvailabilityDAO availabilityDAO) {
		this.availabilityDAO = availabilityDAO;
	}

	@Override
	@Transactional
	public Availability saveAvailability(Availability availability) {
		return availabilityDAO.saveAvailability(availability);
	}

	@Override
	public List<Availability> getAvailabilityByDoctorId(String doctorId) {
		return availabilityDAO.getAvailabilityByDoctorId(doctorId);
	}

	@Override
	@Transactional
	public void deleteAvailabilitybyDoctorIdandAvailDate(String doctorId, Date availDate) {
		availabilityDAO.deleteAvailabilitybyDoctorIdandAvailDate(doctorId, availDate);

	}

	@Override
	public Availability getAvailabilityByDocIdAndTime(String doctorId, Date appointmentDate, String timeSlot) {
		return availabilityDAO.getAvailabilityByDocIdAndTime(doctorId, appointmentDate, timeSlot);
	}

}
