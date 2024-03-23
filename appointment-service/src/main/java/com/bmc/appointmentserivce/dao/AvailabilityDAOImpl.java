package com.bmc.appointmentserivce.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bmc.appointmentserivce.model.entity.Availability;

/**
 * This class performs the CRUD operations on Availability
 *
 */
@Repository
public class AvailabilityDAOImpl implements AvailabilityDAO {

	private EntityManager entityManager;

	@Autowired
	public AvailabilityDAOImpl(EntityManager theEntityManager) {
		entityManager = theEntityManager;
	}

	@Override
	public Availability saveAvailability(Availability availability) {
		return entityManager.merge(availability);
	}

	@Override
	public List<Availability> getAvailabilityByDoctorId(String doctorId) {
		// create a query
		TypedQuery<Availability> theQuery = entityManager
				.createQuery("SELECT avail FROM Availability avail WHERE avail.doctorId = ?1", Availability.class);
		theQuery.setParameter(1, doctorId);

		// execute query and get result list
		List<Availability> availabilities = theQuery.getResultList();

		// return the results
		return availabilities;
	}

	@Override
	public void deleteAvailabilitybyDoctorIdandAvailDate(String doctorId, Date availDate) {
		Query theQuery = entityManager.createQuery("DELETE FROM Availability avail WHERE avail.doctorId = ?1 "
				+ "and avail.availabilityDate = ?2");
		theQuery.setParameter(1, doctorId);
		theQuery.setParameter(2, availDate);
		theQuery.executeUpdate();
	}

	@Override
	public Availability getAvailabilityByDocIdAndTime(String doctorId, Date appointmentDate, String timeSlot) {
		// create a query
		TypedQuery<Availability> theQuery = entityManager
				.createQuery("SELECT avail FROM Availability avail WHERE avail.doctorId = ?1 "
						+ "and avail.availabilityDate = ?2 and avail.timeSlot = ?3", Availability.class);
		theQuery.setParameter(1, doctorId);
		theQuery.setParameter(2, appointmentDate);
		theQuery.setParameter(3, timeSlot);

		// execute query and get result
		Availability availability = theQuery.getSingleResult();

		// return the results
		return availability;
	}
}
