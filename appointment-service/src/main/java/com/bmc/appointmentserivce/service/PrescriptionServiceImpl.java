package com.bmc.appointmentserivce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.bmc.appointmentserivce.dao.PrescriptionRepository;
import com.bmc.appointmentserivce.model.Prescription;
import com.bmc.appointmentserivce.model.entity.Appointment;

/**
 * This Service class reaches out to DAO layer and sends the Prescription object to Kafka topic
 *
 */
@Service
public class PrescriptionServiceImpl implements PrescriptionService {
	
	private PrescriptionRepository prescriptionRepository;
	
	@Value("${kafka.topic}")
	private String kafkaTopic;
	private KafkaTemplate<String,Object> kafkaTemplate;
	private AppointmentServiceImpl appointmentServiceImpl;
	
	@Autowired
	public PrescriptionServiceImpl(PrescriptionRepository prescriptionRepository, 
			KafkaTemplate<String,Object> kafkaTemplate,AppointmentServiceImpl appointmentServiceImpl) {
		this.prescriptionRepository = prescriptionRepository;
		this.kafkaTemplate = kafkaTemplate;
		this.appointmentServiceImpl = appointmentServiceImpl;
	}

	@Override
	public void addPrescription(Prescription prescription) {	
		prescriptionRepository.save(prescription);
		updatePrescriptionWithUserDetails(prescription);
		sendMessagetoNotificationService(prescription);
	}
	
	private void updatePrescriptionWithUserDetails(Prescription prescription) {
		Appointment appointment = appointmentServiceImpl.getAppointmentById(prescription.getAppointmentId());
		prescription.setAppointmentDate(appointment.getAppointmentDate().toString());
		prescription.setUserName(appointment.getUserName());
		prescription.setUserEmailId(appointment.getUserEmailId());
		prescription.setDoctorName(appointment.getDoctorName());
		prescription.setAppointmentTimeSlot(appointment.getTimeSlot());
	}

	/**
	 * This method sends the asynchronous notification message to Kafka topic
	 * @param prescription
	 * @return 
	 */
	private void sendMessagetoNotificationService(Prescription prescription) {
		kafkaTemplate.send(kafkaTopic, "Prescription", prescription);
	}

}
