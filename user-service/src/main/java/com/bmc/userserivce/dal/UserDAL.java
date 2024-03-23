package com.bmc.userserivce.dal;

import com.bmc.userserivce.model.User;

/**
 * This interface to perform the CRUD operations on User Object in Mongo DB
 *
 */
public interface UserDAL {
	
	User getUserById(String UserId);

	User saveUser(User user);

}
