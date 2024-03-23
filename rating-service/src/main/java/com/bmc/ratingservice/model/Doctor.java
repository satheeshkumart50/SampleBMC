package com.bmc.ratingservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Doctor {
	private String id;
	private String firstName;
	private String lastName;
	private String dob;
	private String mobile;
	private String emailId;
	private String pan;
	private String speciality;
	private String status;
	private String approvedBy;
	private String approverComments;
	private String registrationDate;
	private String verificationDate;
	private String rating;
}