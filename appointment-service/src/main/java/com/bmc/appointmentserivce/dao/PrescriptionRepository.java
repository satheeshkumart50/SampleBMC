package com.bmc.appointmentserivce.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bmc.appointmentserivce.model.Prescription;

/**
 * This interface extends the MongoRepository to perform the CRUD operations on PrescriptionRepository
 *
 */
@Repository
public interface PrescriptionRepository extends MongoRepository<Prescription, String> {

}
