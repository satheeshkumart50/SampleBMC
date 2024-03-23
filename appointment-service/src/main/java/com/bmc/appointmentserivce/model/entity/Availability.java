package com.bmc.appointmentserivce.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "availability")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Availability {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="availability_date")
	@Temporal(TemporalType.DATE)
	private Date availabilityDate;
	
	@Column(name="doctor_id")
	private String doctorId;
	
	@Column(name="is_booked")
	@NotNull
	private boolean isBooked;
	
	@Column(name="time_slot")
	private String timeSlot;

}
