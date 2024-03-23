package com.bmc.paymentservice.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bmc.paymentservice.model.Payment;
import com.bmc.paymentservice.service.PaymentService;

/**
 * Contains the controller methods for all end-points related to Payments
 *
 */
@RestController
@RequestMapping("/payments")
public class PaymentController {

	private PaymentService paymentService;

	@Autowired
	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	/**
	 * This method saves the appointment object to Mongo DB
	 * @param authToken
	 * @param appointmentId
	 * @return
	 * @throws ParseException
	 */
	@PostMapping
	public ResponseEntity<Payment> savePayment(@RequestHeader("Authorization") String authToken,@RequestParam String appointmentId) throws ParseException {
		Payment payment = new Payment();
		payment.setId(UUID.randomUUID().toString());
		payment.setAppointmentId(appointmentId);
		Date date = new Date();
		payment.setCreatedDate(date);
		
		Payment _payment  = paymentService.savePayment(payment);
		paymentService.updatePaymentStatus(appointmentId, authToken);
		return new ResponseEntity<Payment>(_payment, HttpStatus.OK);
	}

}
