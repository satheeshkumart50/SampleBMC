package com.bmc.ratingservice.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bmc.ratingservice.model.Rating;

/**
 * This interface extends MongoRepository to perform CRUD operation on Rating object in Mongo DB
 *
 */
@Repository
public interface RatingRepositry extends MongoRepository<Rating, String> {

}
