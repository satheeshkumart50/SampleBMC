package com.bmc.appointmentserivce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bmc.appointmentserivce.exception.PendingPaymentException;
import com.bmc.appointmentserivce.model.ErrorModel;
import com.bmc.appointmentserivce.model.Prescription;
import com.bmc.appointmentserivce.model.entity.Appointment;
import com.bmc.appointmentserivce.service.AppointmentService;
import com.bmc.appointmentserivce.service.PrescriptionService;

/**
 * Contains the controller methods for all end-points related to Prescription *
 */
@RestController
@RequestMapping("/prescriptions")
public class PrescriptionController {

	private PrescriptionService prescriptionService;
	private AppointmentService appointmentService;

	@Value("${error.appointment.pendingPayment}")
	private String ePendingPayment;
	
	@Value("${errorCode.appointment.pendingPayment}")
	private String ePendingPaymentCode;
	
	@Value("${status.payment.pending}")
	private String pendingStatus;
	
	@Value("${status.payment.confirm}")
	private String confirmStatus;

	@Autowired
	public PrescriptionController(PrescriptionService prescriptionService, AppointmentService appointmentService) {
		this.prescriptionService = prescriptionService;
		this.appointmentService = appointmentService;
	}

	/**
	 * This method adds the Prescription to the Mongo DB
	 * @param prescription
	 * @throws PendingPaymentException
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void addPrescription(@RequestBody Prescription prescription)
			throws PendingPaymentException {

		String appointmentId = prescription.getAppointmentId();
		Appointment _appointment = appointmentService.getAppointmentById(appointmentId);
		if (_appointment == null || _appointment.getStatus().equals(pendingStatus) ||
				!_appointment.getStatus().equals(confirmStatus)) {
			throw new PendingPaymentException(ePendingPayment);
		}

		prescriptionService.addPrescription(prescription);
	}

	@ExceptionHandler(PendingPaymentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorModel handlePendingPaymentException(Exception ex) {
		ErrorModel errorModel = new ErrorModel();
		errorModel.setErrorMessage(ex.getMessage());
		errorModel.setErrorCode(ePendingPaymentCode);
		return errorModel;
	}
}
