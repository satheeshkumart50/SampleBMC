package com.bmc.appointmentserivce.model.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"appointmentId","doctorId","userId","timeSlot","status","appointmentDate"})
public class AppointmentDTO {
	
	private String appointmentId;
	private String doctorId;
	private String userId;
	private String timeSlot;
	private String status;
	private String appointmentDate;
}
