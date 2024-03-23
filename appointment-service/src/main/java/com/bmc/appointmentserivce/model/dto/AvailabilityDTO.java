package com.bmc.appointmentserivce.model.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"doctorId","availabilityMap"})
public class AvailabilityDTO {
	
	private String doctorId;
	private Map<String,String[]> availabilityMap;

}
