package com.bmc.userserivce.service;

import org.springframework.web.multipart.MultipartFile;

import com.bmc.userserivce.model.User;

public interface UserService {

	User getUserById(String userId);

	User saveUser(User user);
	
	void uploadFiles(String userId, MultipartFile file); 
	
	String[] getFileNames(String key);

}
