package com.bmc.userserivce.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id","firstName","lastName","speciality","dob","mobile","emailId","createdDate"})
public class User {
	@Id
	private String id;

	@NotEmpty
	@Size(min = 1, max = 20)
	private String firstName;

	@NotEmpty
	@Size(min = 1, max = 20)
	private String lastName;

	@NotEmpty
	@JsonFormat(pattern = "YYYY-MM-dd")
	private String dob;

	@NotBlank
	@Pattern(regexp = "^\\d{10,10}$")
	private String mobile;

	@NotEmpty
	@Email(flags = { Flag.CASE_INSENSITIVE })
	private String emailId;

	private String createdDate;
}