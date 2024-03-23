package com.bmc.appointmentserivce.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Prescription")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Prescription {
	
	@Id
	private String id;
	private String userId;
	private String doctorId;
	private String doctorName;
	private String appointmentId;
	private String diagnosis;
	private List<Medicine> medicineList;
	private String userName;
    private String userEmailId;
	private String appointmentDate;
	private String appointmentTimeSlot;
}
