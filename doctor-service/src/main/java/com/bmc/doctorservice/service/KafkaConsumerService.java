package com.bmc.doctorservice.service;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.bmc.doctorservice.dal.DoctorDAL;
import com.bmc.doctorservice.model.Doctor;
import com.bmc.doctorservice.model.Rating;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This service class listens to the Kafka topic and updates the ratings of the doctor in the Mongo DB
 *
 */
@Service
public class KafkaConsumerService implements ConsumerService {

	private DoctorDAL doctorDAL;

	@Autowired
	public KafkaConsumerService(DoctorDAL doctorDAL) {
		this.doctorDAL = doctorDAL;
	}

	/**
	 *Polls the Kafka topic continuously and picks up the Rating object sent by Rating services
	 */
	@Override
	@KafkaListener(topics = "${consumer.topic}", groupId = "${group.id}", containerFactory = "concurrentKafkaListenerContainerFactory")
	public void listen(ConsumerRecord<String, String> record) throws IOException {
		String receivedMessage = record.value();
		String messageKey = record.key();
		System.out.println(messageKey+"===="+receivedMessage);
		ObjectMapper mapper = new ObjectMapper();
		if(messageKey.equals("Rating")) {
			Rating rating = mapper.readValue(receivedMessage, Rating.class);
			updateRating(rating);
		}
	}
	
	/**
	 *This method calculates the average rating of the doctor ad updates to the DB.
	 */
	private void updateRating(Rating _rating) {
		Doctor doctor = doctorDAL.getDoctorById(_rating.getDoctorId());
		String rating = _rating.getRating();
		String previousRating = doctor.getRating();
		if(previousRating == null || "".equals(previousRating)) {
			doctor.setRating(rating);
		} else {
			Double dRating = Double.parseDouble(rating);
			Double dPreviousRating = Double.parseDouble(previousRating);
			Double averageRating = (dRating+dPreviousRating)/2;
			doctor.setRating(averageRating.toString());
		}
		doctorDAL.saveDoctor(doctor);
	} 

}
