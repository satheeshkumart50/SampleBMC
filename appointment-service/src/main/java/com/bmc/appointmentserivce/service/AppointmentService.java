package com.bmc.appointmentserivce.service;

import java.util.List;

import com.bmc.appointmentserivce.model.entity.Appointment;

public interface AppointmentService {

	Appointment saveAppointment(Appointment appointment);
	
	Appointment updateAppointment(Appointment appointment);

	Appointment getAppointmentById(String appointmentId);

	List<Appointment> getAppointmentByUserId(String userId);

}
