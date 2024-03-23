package com.bmc.doctorservice.dal;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.bmc.doctorservice.model.Doctor;

/**
 * This class performs the CRUD operations on Doctor Object in Mongo DB
 *
 */
@Repository
@CacheConfig(cacheNames = {"doctor"})
public class DocterDALImpl implements DoctorDAL{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Value("${query.doctor.limit}")
	private int queryLimit;
	@Value("${query.doctor.sortField}")
	private String sortField;
	@Value("${query.doctor.status}")
	private String doctorStatus;
	@Value("${query.doctor.speciality}")
	private String doctorSpeciality;

	@Override
	@Cacheable
	public List<Doctor> getAllDoctors() {
		return mongoTemplate.findAll(Doctor.class);
	}

	@Override
	@Cacheable
	public Doctor getDoctorById(String doctorId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(doctorId));
		return mongoTemplate.findOne(query, Doctor.class);
	}

	@Override
	public Doctor saveDoctor(Doctor doctor) {
		mongoTemplate.save(doctor);
		return doctor;
	}

	@Override
	public List<Doctor> getDoctorsByStatus(String status) {
		Query query = new Query();
		query.addCriteria(Criteria.where(doctorStatus).is(status));
		query.with(Sort.by(Sort.Direction.DESC,sortField));
		query.collation(Collation.of(Locale.US).numericOrdering(true));
		query.limit(queryLimit);
		return mongoTemplate.find(query,Doctor.class);
	}

	@Override
	public List<Doctor> getDoctorsBySpeciality(String speciality) {
		Query query = new Query();
		query.addCriteria(Criteria.where(doctorSpeciality).is(speciality));
		query.with(Sort.by(Sort.Direction.DESC,sortField));
		query.collation(Collation.of(Locale.US).numericOrdering(true));
		query.limit(queryLimit);
		return mongoTemplate.find(query,Doctor.class);
	}

	@Override
	public List<Doctor> getDoctorsByStatusAndSpeciality(String status, String speciality) {
		Query query = new Query();
		query.addCriteria(Criteria.where(doctorStatus).is(status));
		query.addCriteria(Criteria.where(doctorSpeciality).is(speciality));
		query.with(Sort.by(Sort.Direction.DESC,sortField));
		query.collation(Collation.of(Locale.US).numericOrdering(true));
		query.limit(queryLimit);
		return mongoTemplate.find(query,Doctor.class);
	}

}
