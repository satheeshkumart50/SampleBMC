package com.bmc.doctorservice.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * Creates the S3Config object and configures the  bootstrapServer to the S3Config object
 */
@Configuration
public class S3Config {

	private AWSCredentials credentials;
	
	@Value("${s3.accessKey}")
	private String accessKey;
	@Value("${s3.secretKey}")
	private String secretKey;

	@PostConstruct
	public void init(){
		credentials = new BasicAWSCredentials(accessKey,secretKey);
	}

	@Bean
	public AmazonS3 createS3CLient() {
		AmazonS3 s3Client = AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_EAST_1)
				.build();
		return s3Client;
	}

	@Bean
	public ObjectMetadata createObjectMetadata(){
		return new ObjectMetadata();
	}
}
