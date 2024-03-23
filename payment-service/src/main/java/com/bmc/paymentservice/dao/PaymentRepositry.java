package com.bmc.paymentservice.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bmc.paymentservice.model.Payment;


/**
 * This interfaces extends the MongoRepository to perform the CRUD operation on Payment object
 *
 */
@Repository
public interface PaymentRepositry extends MongoRepository<Payment, String> {

}
