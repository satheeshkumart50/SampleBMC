package com.bmc.appointmentserivce.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bmc.appointmentserivce.model.Doctor;
import com.bmc.appointmentserivce.model.ErrorModel;
import com.bmc.appointmentserivce.model.User;
import com.bmc.appointmentserivce.model.dto.AppointmentDTO;
import com.bmc.appointmentserivce.model.entity.Appointment;
import com.bmc.appointmentserivce.model.entity.Availability;
import com.bmc.appointmentserivce.service.AppointmentService;
import com.bmc.appointmentserivce.service.AvailabilityService;
import com.bmc.appointmentserivce.service.feign.DoctorServiceClient;
import com.bmc.appointmentserivce.service.feign.UserServiceClient;

/**
 * Contains the controller methods for all end-points related to Appointments
 *
 */
@RestController
public class AppointmentController {

	private AppointmentService appointmentService;
	private AvailabilityService availabilityService;
	private UserServiceClient userServiceClient;
	private DoctorServiceClient doctorServiceClient;
	private SimpleDateFormat formatter;
	
	@Value("${status.payment.pending}")
	private String pendingStatus;
	@Value("${status.payment.confirm}")
	private String confirmStatus;
	
	@Value("${error.doctor.doctorNotFound}")
	private String eDoctorNotFound;
	@Value("${errorCode.doctor.doctorNotFound}")
	private String eDoctorNotFoundCode;
	
	@Value("${error.user.userNotFound}")
	private String eUserNotFound;
	@Value("${errorCode.user.userNotFound}")
	private String eUserNotFoundCode;

	@Autowired
	public AppointmentController(AppointmentService appointmentService, AvailabilityService availabilityService,
			UserServiceClient userServiceClient, DoctorServiceClient doctorServiceClient) {
		this.appointmentService = appointmentService;
		this.availabilityService = availabilityService;
		this.userServiceClient = userServiceClient;
		this.doctorServiceClient = doctorServiceClient;
	}

	@PostConstruct
	public void init() {
		formatter = new SimpleDateFormat("yyyy-MM-dd");
	}

	/**
	 * This method adds the Appointment object to the SQL Appointment table after checking for Doctor and User ids
	 * 
	 * @param authToken
	 * @param appointmentDTO
	 * @return
	 * @throws ParseException
	 */
	@PostMapping("/appointments")
	public ResponseEntity<String> addAppointment(@RequestHeader("Authorization") String authToken,
			@RequestBody AppointmentDTO appointmentDTO) throws ParseException {
		

		Appointment appointment = new Appointment();
		
		// Below code reaches out to Doctor services to check for the Doctor Id and get the Doctor's name
		String doctorId = appointmentDTO.getDoctorId();
		appointment.setDoctorId(doctorId);
		Doctor doctor = doctorServiceClient.getDoctor(authToken, doctorId);
		appointment.setDoctorName(doctor.getFirstName() + " " + doctor.getLastName());

		// Below code reaches out to User services to check for the User Id and get the User's email id
		String userId = appointmentDTO.getUserId();
		appointment.setUserId(userId);
		User user = userServiceClient.getUser(authToken, userId);
		appointment.setUserName(user.getFirstName() + " " + user.getLastName());
		appointment.setUserEmailId(user.getEmailId());

		String date = appointmentDTO.getAppointmentDate();
		Date appointmentDate = formatter.parse(date);
		appointment.setAppointmentDate(appointmentDate);
		String timeSlot = appointmentDTO.getTimeSlot();
		appointment.setTimeSlot(timeSlot);
		appointment.setStatus(pendingStatus);

		//Below code updates the Doctor's availability as booked during the requested time slot and saves the appointment object
		Availability availability = availabilityService.getAvailabilityByDocIdAndTime(doctorId, appointmentDate, timeSlot);
		availability.setBooked(true);
		availabilityService.saveAvailability(availability);

		Appointment _appointment = appointmentService.saveAppointment(appointment);

		return new ResponseEntity<String>(_appointment.getAppointmentId(), HttpStatus.OK);

	}

	/**
	 * This method gets the details of the appointment for the requested appointment id
	 * @param appointmentId
	 * @return
	 */
	@GetMapping("appointments/{appointmentId}")
	public ResponseEntity<AppointmentDTO> getAppointmentById(@PathVariable("appointmentId") String appointmentId) {

		AppointmentDTO appointmentDTO = null;
		Appointment _appointment = appointmentService.getAppointmentById(appointmentId);

		if (_appointment != null) {
			appointmentDTO = new AppointmentDTO();
			appointmentDTO.setAppointmentId(String.valueOf(_appointment.getAppointmentId()));
			appointmentDTO.setDoctorId(_appointment.getDoctorId());
			appointmentDTO.setUserId(_appointment.getUserId());
			appointmentDTO.setAppointmentDate(formatter.format(_appointment.getAppointmentDate()));
			appointmentDTO.setTimeSlot(_appointment.getTimeSlot());
			appointmentDTO.setStatus(_appointment.getStatus());
		}
		
		return new ResponseEntity<AppointmentDTO>(appointmentDTO, HttpStatus.OK);
	}
	
	
	/**
	 * This method updates the status of the Appointment from Pending Payment to Confirmed.
	 * @param appointmentId
	 * @return
	 */
	@PutMapping("appointments/{appointmentId}")
	public ResponseEntity<String> updateAppointmentStatus(@PathVariable("appointmentId") String appointmentId) {
		Appointment _appointment = appointmentService.getAppointmentById(appointmentId);
		_appointment.setStatus(confirmStatus);
		appointmentService.updateAppointment(_appointment);
		
		return new ResponseEntity<String>(confirmStatus, HttpStatus.OK);
	}

	/**
	 * 
	 *This method gets all the appointment done by one particular user
	 * @param userId
	 * @return
	 */
	@GetMapping("users/{userId}/appointments")
	public ResponseEntity<List<AppointmentDTO>> getAppointmentByUserId(@PathVariable("userId") String userId) {

		AppointmentDTO appointmentDTO = null;
		List<AppointmentDTO> appointmentDTOs = null;
		List<Appointment> appointments = appointmentService.getAppointmentByUserId(userId);

		if (appointments.size() > 0) {
			appointmentDTOs = new ArrayList<AppointmentDTO>(appointments.size());

			for (Appointment _appointment : appointments) {
				appointmentDTO = new AppointmentDTO();
				appointmentDTO.setAppointmentId(String.valueOf(_appointment.getAppointmentId()));
				appointmentDTO.setDoctorId(_appointment.getDoctorId());
				appointmentDTO.setUserId(_appointment.getUserId());
				appointmentDTO.setAppointmentDate(formatter.format(_appointment.getAppointmentDate()));
				appointmentDTO.setTimeSlot(_appointment.getTimeSlot());
				appointmentDTO.setStatus(_appointment.getStatus());
				appointmentDTOs.add(appointmentDTO);
			}
		}

		return new ResponseEntity<List<AppointmentDTO>>(appointmentDTOs, HttpStatus.OK);

	}
	
	/**
	 * This method handles the exception and returns the Error message to the user when the invalid Doctor id or
	 *  user id is provided as input
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(feign.FeignException.NotFound.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorModel handleRequestedResourceNotFoundException(Exception e) {
		ErrorModel errorModel = new ErrorModel();
		if (e.getMessage().toString().indexOf("DoctorServiceClient") > -1) {
			errorModel.setErrorMessage(eDoctorNotFound);
			errorModel.setErrorCode(eDoctorNotFoundCode); 
		} 
		
		if (e.getMessage().toString().indexOf("UserServiceClient") > -1) {
			errorModel.setErrorMessage(eUserNotFound);
			errorModel.setErrorCode(eUserNotFoundCode); 
		}
		return errorModel;
	}

}
