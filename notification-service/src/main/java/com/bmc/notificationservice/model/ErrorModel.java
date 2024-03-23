package com.bmc.notificationservice.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"errorCode","errorMessage"})
public class ErrorModel {

	private String errorCode;
	private String errorMessage;
}
