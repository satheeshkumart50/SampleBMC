package com.bmc.ratingservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bmc.ratingservice.model.Rating;
import com.bmc.ratingservice.model.ErrorModel;
import com.bmc.ratingservice.service.RatingService;
import com.bmc.ratingservice.service.feign.DoctorServiceClient;

/**
 * Contains the controller methods for all end-points related to Ratings
 *
 */
@RestController
@RequestMapping("/ratings")
public class RatingController {

	private RatingService ratingService;
	private DoctorServiceClient doctorServiceClient;
	
	@Value("${error.doctor.doctorNotFound}")
	private String eDoctorNotFound;
	@Value("${errorCode.doctor.doctorNotFound}")
	private String eDoctorNotFoundCode;

	@Autowired
	public RatingController(RatingService ratingService, DoctorServiceClient doctorServiceClient) {
		this.ratingService = ratingService;
		this.doctorServiceClient = doctorServiceClient;
	}

	/**
	 * This method checks for the correct Doctor Id and updates the Rating of the doctor in Mongo DB
	 * @param authToken
	 * @param rating
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void saveRating(@RequestHeader("Authorization") String authToken,@RequestBody Rating rating) {
		doctorServiceClient.getDoctor(authToken, rating.getDoctorId());
		ratingService.saveRating(rating);
	}
	
	/**
	 * This exception handler throws the proper error message when invalid doctor id provided
	 */
	@ExceptionHandler(feign.FeignException.NotFound.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorModel handleRequestedResourceNotFoundException() {
		ErrorModel errorModel = new ErrorModel();
		errorModel.setErrorMessage(eDoctorNotFound);
		errorModel.setErrorCode(eDoctorNotFoundCode);
		return errorModel;
	}

}
