package com.bmc.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medicine {
	
	private String name;
	private String dosage;
	private String frequency;
	private String remarks;
}
