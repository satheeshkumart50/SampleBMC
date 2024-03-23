package com.bmc.appointmentserivce.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "appointment")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

	@Id
	@GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name="appointment_id",unique=true,columnDefinition="VARCHAR(64)")
	private String appointmentId;
	
	@Column(name="appointment_date")
	@Temporal(TemporalType.DATE)
	private Date appointmentDate;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="doctor_id")
	private String doctorId;
	
	@Column(name="prior_medical_history")
	private String priorMedicalHistory;
	
	@Column(name="status")
	private String status;
	
	@Column(name="symptoms")
	private String symptoms;
	
	@Column(name="time_slot")
	private String timeSlot;
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name="user_email_id")
	private String userEmailId;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="doctor_name")
	private String doctorName;
}
