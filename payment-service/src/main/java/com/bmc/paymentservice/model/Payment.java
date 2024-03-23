package com.bmc.paymentservice.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
	
	@Id
	private String id;
	private String appointmentId;
	private Date createdDate;
}
