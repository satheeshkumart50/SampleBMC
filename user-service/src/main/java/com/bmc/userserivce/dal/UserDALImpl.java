package com.bmc.userserivce.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.bmc.userserivce.model.User;

/**
 * This class performs the CRUD operations on User Object in Mongo DB
 *
 */
@Repository
public class UserDALImpl implements UserDAL{
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	@Cacheable
	public User getUserById(String userId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(userId));
		return mongoTemplate.findOne(query, User.class);
	}

	@Override
	public User saveUser(User user) {
		mongoTemplate.save(user);
		return user;
	}

}
