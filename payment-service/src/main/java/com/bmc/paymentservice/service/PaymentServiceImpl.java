package com.bmc.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bmc.paymentservice.dao.PaymentRepositry;
import com.bmc.paymentservice.model.Payment;

/**
 * This services reaches out to DAO layer to perform the CRUD operation on Payment object
 * and reaches out to appointment services to update the Payment status in Appointment Object
 *
 */
@Service
public class PaymentServiceImpl implements PaymentService {

	private PaymentRepositry paymentRepositry;
	private RestTemplate restTemplate;

	@Value("${gateway.service.url}")
	private String appointmentServiceUrl;

	@Value("${kafka.topic}")
	private String kafkaTopic;
	private KafkaTemplate<String,Payment> kafkaTemplate;

	@Autowired
	public PaymentServiceImpl(PaymentRepositry paymentRepositry, KafkaTemplate<String,Payment> kafkaTemplate,
			RestTemplate restTemplate) {
		this.paymentRepositry = paymentRepositry;
		this.kafkaTemplate = kafkaTemplate;
		this.restTemplate = restTemplate;
	}

	@Override
	public Payment savePayment(Payment payment) {
		Payment _payment = paymentRepositry.save(payment);
		sendMessagetoNotificationService(payment);
		return _payment;
	}

	/**
	 *Reaches out to appointment services to update the Payment status in Appointment Object
	 */
	@Override
	public String updatePaymentStatus(String appointmentId,String authToken) {

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", authToken);
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		return restTemplate.exchange(
				appointmentServiceUrl+"/appointments/"+appointmentId, HttpMethod.PUT, entity, String.class).getBody();
	}


	/**
	 * This method sends the asynchronous notification message to Kafka topic
	 * @param _payment
	 * @return 
	 */
	private void sendMessagetoNotificationService(Payment payment) {
		kafkaTemplate.send(kafkaTopic, "Payment", payment);
	}
}
