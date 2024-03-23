package com.bmc.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

	private String appointmentId;
	private String appointmentDate;
	private String createdDate;
	private String doctorId;
	private String priorMedicalHistory;
	private String status;
	private String symptoms;
	private String timeSlot;
	private String userId;
	private String userEmailId;
	private String userName;
	private String doctorName;
}
