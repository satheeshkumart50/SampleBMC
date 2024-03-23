package com.bmc.paymentservice.service;

import com.bmc.paymentservice.model.Payment;

public interface PaymentService {

	Payment savePayment(Payment payment);
	String updatePaymentStatus(String appointmentId,String authToken);

}
