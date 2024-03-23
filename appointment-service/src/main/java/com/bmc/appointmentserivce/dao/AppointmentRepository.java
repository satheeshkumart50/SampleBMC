package com.bmc.appointmentserivce.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bmc.appointmentserivce.model.entity.Appointment;


/**
 * This interface extends the JPARepositry to perform the CRUD operations on Appointment
 *
 */
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String> {
	List<Appointment> getAppointmentByUserId(String userId);
}
