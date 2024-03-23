package com.bmc.appointmentserivce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.bmc.appointmentserivce.dao.AppointmentRepository;
import com.bmc.appointmentserivce.model.entity.Appointment;

/**
 * This Service class reaches out to DAO layer and sends the appointment object to Kafka topic
 *
 */
@Service
public class AppointmentServiceImpl implements AppointmentService {

	private AppointmentRepository appointmentRepository;
	
	@Value("${kafka.topic}")
	private String kafkaTopic;
	private KafkaTemplate<String,Object> kafkaTemplate;

	@Autowired
	public AppointmentServiceImpl(AppointmentRepository appointmentRepository,KafkaTemplate<String, Object> kafkaTemplate) {
		this.appointmentRepository = appointmentRepository;
		this.kafkaTemplate = kafkaTemplate;
	}
	
	@Override
	public Appointment saveAppointment(Appointment appointment) {
		Appointment _appointment = appointmentRepository.save(appointment);
		sendMessagetoNotificationService(appointment);
		return _appointment;
	}
	
	@Override
	public Appointment updateAppointment(Appointment appointment) {
		Appointment _appointment = appointmentRepository.save(appointment);
		return _appointment;
	}
	
	@Override
	public Appointment getAppointmentById(String appointmentId) {
		return appointmentRepository.getById(appointmentId);
	}
	
	/**
	 * This method sends the asynchronous notification message to Kafka topic
	 * @param appointment
	 * @return 
	 */
	private void sendMessagetoNotificationService(Appointment appointment) {
		kafkaTemplate.send(kafkaTopic, "Appointment", appointment);
	}

	@Override
	public List<Appointment> getAppointmentByUserId(String userId) {
		return appointmentRepository.getAppointmentByUserId(userId);
	}

}
