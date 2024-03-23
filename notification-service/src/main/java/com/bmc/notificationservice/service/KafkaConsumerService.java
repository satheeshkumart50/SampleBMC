package com.bmc.notificationservice.service;

import java.io.IOException;

import javax.mail.MessagingException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bmc.notificationservice.model.Appointment;
import com.bmc.notificationservice.model.Doctor;
import com.bmc.notificationservice.model.Prescription;
import com.bmc.notificationservice.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import freemarker.template.TemplateException;

/**
 * This service class listens to the Kafka topic and sends email using the AWS smtp server for the following condition
 * Doctor & User verification
 * Doctor Approval/Rejection
 * Appointment confirmation
 * Prescription update
 */
@Service
public class KafkaConsumerService implements ConsumerService{
	
	@Autowired
	private MailService mailService;
    
	@Override
    @KafkaListener(topics = "${consumer.topic}", groupId = "${group.id}", containerFactory = "concurrentKafkaListenerContainerFactory")
	public void listen(ConsumerRecord<String, String> record) throws IOException, TemplateException, MessagingException{
		String receivedMessage = record.value();
		String messageKey = record.key();
		System.out.println(messageKey+"=="+receivedMessage);
		ObjectMapper mapper = new ObjectMapper();
		if(messageKey.equals("Doctor")) {
			Doctor doctor = mapper.readValue(receivedMessage, Doctor.class);
			if(doctor.getStatus().equals("Pending")) {
				mailService.verifyEmail(doctor.getEmailId());
			} else if(doctor.getStatus().equals("Rejected")) {
				mailService.sendDoctorRegistrationEmail(doctor);
			} else if(doctor.getStatus().equals("Active")) {
				doctor.setStatus("Confirmed");
				mailService.sendDoctorRegistrationEmail(doctor);
			} 
		} else if(messageKey.equals("User")) {
			User user = mapper.readValue(receivedMessage, User.class);
			mailService.verifyEmail(user.getEmailId());
		} else if(messageKey.equals("Appointment")) {
			Appointment appointment = mapper.readValue(receivedMessage, Appointment.class);
			mailService.sendAppointmentConfirmationEmail(appointment);
		} else if(messageKey.equals("Prescription")) {
			Prescription prescription = mapper.readValue(receivedMessage, Prescription.class);
			mailService.sendPrescriptionEmail(prescription);
		}
	}
}
