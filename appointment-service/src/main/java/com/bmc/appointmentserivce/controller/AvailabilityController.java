package com.bmc.appointmentserivce.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bmc.appointmentserivce.model.ErrorModel;
import com.bmc.appointmentserivce.model.dto.AvailabilityDTO;
import com.bmc.appointmentserivce.model.entity.Availability;
import com.bmc.appointmentserivce.service.AvailabilityService;
import com.bmc.appointmentserivce.service.feign.DoctorServiceClient;

/**
 * Contains the controller methods for all end-points related to Doctor's Availability
 *
 */
@RestController
@RequestMapping("/doctor")
public class AvailabilityController {

	private AvailabilityService availabilityService;
	private DoctorServiceClient doctorServiceClient;
	private SimpleDateFormat formatter;

	@Value("${error.doctor.doctorNotFound}")
	private String eDoctorNotFound;
	@Value("${errorCode.doctor.doctorNotFound}")
	private String eDoctorNotFoundCode;


	@Autowired
	public AvailabilityController(AvailabilityService availabilityService, DoctorServiceClient doctorServiceClient) {
		this.availabilityService = availabilityService;
		this.doctorServiceClient = doctorServiceClient;
	}

	@PostConstruct
	public void init() {
		formatter = new SimpleDateFormat("yyyy-MM-dd");
	}

	/**
	 * This method add the Doctor's Availability date and time slot to SQL Database table
	 * 
	 * @param authToken
	 * @param doctorId
	 * @param availabilityDTO
	 * @return
	 * @throws ParseException
	 */
	@PostMapping("/{doctorId}/availability")
	public ResponseEntity<AvailabilityDTO> addAvailability(@RequestHeader("Authorization") String authToken, 
			@PathVariable String doctorId,
			@RequestBody AvailabilityDTO availabilityDTO) throws ParseException {

		//Checks if provided Doctor id is valid or not, if invalid then throws Not Found exception
		doctorServiceClient.getDoctor(authToken, doctorId);

		Map<String, String[]> availabilityMap = availabilityDTO.getAvailabilityMap();

		for (Map.Entry<String, String[]> entry : availabilityMap.entrySet()) {
			String date = entry.getKey();
			Date availabilityDate = formatter.parse(date);
			
			// Check and deletes the Doctor's availability if the same Date is provided for the second time to avoid duplicates
			availabilityService.deleteAvailabilitybyDoctorIdandAvailDate(doctorId,availabilityDate);
				
			String[] arrTimeSlots = entry.getValue();
			for (String timeSlot : arrTimeSlots) {
				Availability _availability = new Availability();
				_availability.setDoctorId(doctorId);
				_availability.setBooked(false);
				_availability.setAvailabilityDate(availabilityDate);
				_availability.setTimeSlot(timeSlot);
				availabilityService.saveAvailability(_availability);
			}
		}
		availabilityDTO.setDoctorId(doctorId);

		return new ResponseEntity<AvailabilityDTO>(availabilityDTO, HttpStatus.OK);

	}

	/**
	 * This controller method returns the Doctor's availability based on id
	 * @param authToken
	 * @param doctorId
	 * @return
	 * @throws ParseException
	 */
	@GetMapping("/{doctorId}/availability")
	public ResponseEntity<AvailabilityDTO> getAvailabilityByDoctorId(@RequestHeader("Authorization") String authToken,
			@PathVariable String doctorId)
					throws ParseException {

		doctorServiceClient.getDoctor(authToken, doctorId);

		List<Availability> availabilities = availabilityService.getAvailabilityByDoctorId(doctorId);
		AvailabilityDTO availabilityDTO = null;

		if (availabilities.size() > 0) {
			availabilityDTO = new AvailabilityDTO();
			availabilityDTO.setDoctorId(doctorId);
			Map<String, String[]> availabilityMap = null;
			Map<String, String> availabilitiesMap = null;

			availabilitiesMap = new HashMap<String, String>();
			for (Availability availability : availabilities) {
				Date availabilityDate = availability.getAvailabilityDate();

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String date = dateFormat.format(availabilityDate);

				if (availabilitiesMap.containsKey(date))
					availabilitiesMap.put(date, availabilitiesMap.get(date) + " ," + availability.getTimeSlot());
				else
					availabilitiesMap.put(date, availability.getTimeSlot());
			}

			availabilityMap = new HashMap<String, String[]>();
			for (Map.Entry<String, String> entry : availabilitiesMap.entrySet()) {
				String date = entry.getKey();
				availabilityMap.put(date, availabilitiesMap.get(date).split(" ,"));
			}

			availabilityDTO.setAvailabilityMap(availabilityMap);
		}

		return new ResponseEntity<AvailabilityDTO>(availabilityDTO, HttpStatus.OK);

	}
	
	
	/**
	 * This method handles the exception and returns the Error message to the user when the invalid Doctor id 
	 * is provided as input
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(feign.FeignException.NotFound.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorModel handleRequestedResourceNotFoundException(Exception e) {
		ErrorModel errorModel = new ErrorModel();
		errorModel.setErrorMessage(eDoctorNotFound);
		errorModel.setErrorCode(eDoctorNotFoundCode); 
		return errorModel;
	}

}
