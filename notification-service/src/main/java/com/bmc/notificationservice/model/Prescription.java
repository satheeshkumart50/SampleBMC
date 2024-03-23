package com.bmc.notificationservice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Prescription {
	
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
