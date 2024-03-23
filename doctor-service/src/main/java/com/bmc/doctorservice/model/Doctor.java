package com.bmc.doctorservice.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Doctor")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id","firstName","lastName","speciality","dob","mobile","emailId","pan","status",
	"approvedBy","approverComments","registrationDate","verificationDate"})
public class Doctor {
	@Id
	private String id;

	@NotEmpty
	@Size(min = 1, max = 20)
	private String firstName;

	@NotEmpty
	@Size(min = 1, max = 20)
	private String lastName;

	@NotEmpty
//	@JsonFormat(pattern = "YYYY-MM-dd")
	private String dob;

	@NotBlank
	@Pattern(regexp = "^\\d{10,10}$")
	private String mobile;

	@NotEmpty
	@Email(flags = { Flag.CASE_INSENSITIVE })
	private String emailId;

	@NotEmpty
	@Size(min = 10, max = 10)
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z0-9]+$")
	private String pan;

	private String speciality;
	private String status;
	private String approvedBy;
	private String approverComments;
	private String registrationDate;
	private String verificationDate;	
	private String rating;

}