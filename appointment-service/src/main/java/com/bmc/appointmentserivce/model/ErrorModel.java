package com.bmc.appointmentserivce.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"errorCode","errorMessage"})
public class ErrorModel {

	private String errorCode;
	private String errorMessage;
}
