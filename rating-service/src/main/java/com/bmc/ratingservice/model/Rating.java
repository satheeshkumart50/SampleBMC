package com.bmc.ratingservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Rating")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
	
	@Id
	private String id;
	private String doctorId;
	private String rating;
}
