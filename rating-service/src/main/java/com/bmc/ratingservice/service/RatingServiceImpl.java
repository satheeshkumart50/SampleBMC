package com.bmc.ratingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.bmc.ratingservice.dao.RatingRepositry;
import com.bmc.ratingservice.model.Rating;

/**
 * This service class reaches out to DAO layer to perform the Save operation of Ratings object in Mongo DB
 *
 */
@Service
public class RatingServiceImpl implements RatingService {

	private RatingRepositry ratingRepositry;

	@Value("${kafka.topic}")
	private String kafkaTopic;
	private KafkaTemplate<String,Rating> kafkaTemplate;

	@Autowired
	public RatingServiceImpl(RatingRepositry ratingRepositry,
			KafkaTemplate<String,Rating> kafkaTemplate) {
		this.ratingRepositry = ratingRepositry;
		this.kafkaTemplate = kafkaTemplate;
	}

	@Override
	public Rating saveRating(Rating rating) {
		Rating _rating = ratingRepositry.save(rating);
		sendMessagetoNotificationService(_rating);
		return _rating;
	}


	/**
	 * This method sends the asynchronous notification message to Kafka topic
	 * @param rating
	 * @return 
	 */
	private void sendMessagetoNotificationService(Rating rating) {
		kafkaTemplate.send(kafkaTopic, "Rating", rating);
	}
}
