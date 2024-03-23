package com.bmc.appointmentserivce.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	private String id;
	private String firstName;
	private String lastName;
	private String dob;
	private String mobile;
	private String emailId;
	private String createdDate;
}