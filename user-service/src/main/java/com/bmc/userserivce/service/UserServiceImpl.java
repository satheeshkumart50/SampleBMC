package com.bmc.userserivce.service;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.bmc.userserivce.dal.UserDAL;
import com.bmc.userserivce.model.User;

/**
 *This service class reaches out to DAL layer to perform CRUD operations on User's object in Mongo DB
 *Reaches to AWS S3 bucket to upload the files and retrieve the file names
 *Sends the asynchronous notification to Kafka topic
 *
 */
@Service
public class UserServiceImpl implements UserService {

	private UserDAL userDAL;

	@Value("${kafka.topic}")
	private String kafkaTopic;
	private KafkaTemplate<String,User> kafkaTemplate;
	
	@Value("${s3.bucket}")
	private String BUCKET_NAME;
	private AmazonS3 s3Client;
	private ObjectMetadata objectMetadata;

	@Autowired
	public UserServiceImpl(UserDAL userDAL, KafkaTemplate<String,User> kafkaTemplate, 
			AmazonS3 s3Client, ObjectMetadata objectMetadata) {
		this.userDAL = userDAL;
		this.kafkaTemplate = kafkaTemplate;
		this.s3Client = s3Client;
		this.objectMetadata = objectMetadata;
	}

	@Override
	public User getUserById(String userId) {
		return userDAL.getUserById(userId);
	}

	@Override
	public User saveUser(User user) {
		User _user = userDAL.saveUser(user);
		sendMessagetoNotificationService(_user);
		return _user;
	}

	/**
	 *This method uploads the file to AWS S3 Bucket
	 *@param userId
	 *@param file
	 *@return
	 */
	@Override
	public void uploadFiles(String userId, MultipartFile file) {
		try {
		String key = userId + "/"+ file.getOriginalFilename();
		if(!s3Client.doesBucketExistV2(BUCKET_NAME)){
			s3Client.createBucket(BUCKET_NAME);
		}
		s3Client.putObject(BUCKET_NAME,key,file.getInputStream(),objectMetadata);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *This method gets the file names of the files checked in to AWS S3 Bucket
	 *@param key
	 *@return  String[]
	 */
	@Override
	public String[] getFileNames(String key) {
		ArrayList<String> alFiles;
		String [] files = null;
		try {   
			ObjectListing objectListings = s3Client.listObjects(BUCKET_NAME,key);

			/* Recursively get all the objects inside given bucket */
			if(objectListings != null && objectListings.getObjectSummaries() != null) { 
				alFiles = new ArrayList<String>();
				while (true) {                  
					for(S3ObjectSummary summary : objectListings.getObjectSummaries()) {
						alFiles.add(summary.getKey().toString().substring(key.length()+1));
					}

					if (objectListings.isTruncated()) {
						objectListings = s3Client.listNextBatchOfObjects(objectListings);
					} else {
						break;
					}                   
				}
				if(alFiles.size() > 0) {
					files =  alFiles.toArray(new String[alFiles.size()]);
				}
			}
			
		} catch (AmazonServiceException e) {
			e.printStackTrace();
		} 
		return files;
	}

	/**
	 * This method sends the asynchronous notification message to Kafka topic
	 * @param _User
	 * @return 
	 */
	private void sendMessagetoNotificationService(User _user) {
		kafkaTemplate.send(kafkaTopic, "User", _user);
	}

}
