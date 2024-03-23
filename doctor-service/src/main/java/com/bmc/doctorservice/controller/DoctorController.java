package com.bmc.doctorservice.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bmc.doctorservice.datacache.CacheStore;
import com.bmc.doctorservice.model.Doctor;
import com.bmc.doctorservice.model.ErrorModel;
import com.bmc.doctorservice.service.DoctorService;

/**
 * Contains the controller methods for all end-points related to Doctor
 *
 */
@RestController
@RequestMapping("/doctors")
public class DoctorController {

	private DoctorService doctorService;
    private CacheStore<Doctor> doctorCache;

	@Value("${default.doctor.speciality}")
	private String defaultSpeciality;
	@Value("${default.doctor.status}")
	private String defaultStatus;
	@Value("${approve.doctor.status}")
	private String approveStatus;
	@Value("${reject.doctor.status}")
	private String rejectStatus;
	@Value("${error.doctor.resourceNotAvailable}")
	private String eResourceNotAvailable;
	@Value("${error.doctor.invalidInput}")
	private String eInvalidInput;
	@Value("${errorCode.doctor.resourceNotAvailable}")
	private String eResourceNotFoundCode;
	@Value("${errorCode.doctor.invalidInput}")
	private String eInavlidInputCode;
	@Value("${error.doctor.fields}")
	private String errorField;
	
	@Autowired
	public DoctorController(DoctorService doctorService, CacheStore<Doctor> doctorCache) {
		this.doctorService = doctorService;
		this.doctorCache = doctorCache;
	}

	/**
	 * Fetches the details of the doctor based on the id provided as input
	 * First check the data in the Cache if not found, then retrieves from DB.
	 * @param doctorId
	 * @return
	 */
	@GetMapping("/{doctorId}")
	public ResponseEntity<Doctor> getDoctorbyId(@PathVariable String doctorId) {
		
		//Search for the Doctor in the cache
		Doctor cachedDoctor = doctorCache.get(doctorId);

        if(cachedDoctor != null){
        	return new ResponseEntity<Doctor>(cachedDoctor, HttpStatus.OK);
        }
		
		Doctor _doctor = doctorService.getDoctorById(doctorId);
		if (_doctor == null)
			throw new NoSuchElementException();
		
		//store into the cache
		doctorCache.add(_doctor.getId(),_doctor);
		return new ResponseEntity<Doctor>(_doctor, HttpStatus.OK);
	}

	/**
	 * Creates the new Doctor in Mongo DB based on the inputs provided by user
	 * @param doctor
	 * @return
	 */
	@PostMapping
	public ResponseEntity<Doctor> addNewDoctor(@RequestBody @Valid Doctor doctor) throws ParseException {
		doctor.setStatus(defaultStatus);
		String dob = doctor.getDob();
		doctor.setDob(convertDateFormat(dob));
		Date date = new Date();
		doctor.setRegistrationDate(convertToString(date));

		if (doctor.getSpeciality() == null || "".equals(doctor.getSpeciality().trim()))
			doctor.setSpeciality(defaultSpeciality);

		Doctor _doctor = doctorService.saveDoctor(doctor);
		
		//store into the cache
		doctorCache.add(_doctor.getId(),_doctor);
		return new ResponseEntity<Doctor>(_doctor, HttpStatus.OK);
	}

	/**
	 * Updates the Doctor's registration as approved in Mongo DB based on the inputs provided by approver
	 * @param doctor
	 * @return
	 */
	@PutMapping("/{doctorId}/approve")
	public ResponseEntity<Doctor> approveDoctor(@PathVariable String doctorId, @RequestBody Doctor doctor)
			throws ParseException {
		Doctor _doctor = doctorService.getDoctorById(doctorId);
		if (_doctor == null)
			throw new NoSuchElementException();
		_doctor.setApprovedBy(doctor.getApprovedBy());
		_doctor.setApproverComments(doctor.getApproverComments());
		_doctor.setStatus(approveStatus);
		Date date = new Date();
		_doctor.setVerificationDate(convertToString(date));

		Doctor updatedDoctor = doctorService.saveDoctor(_doctor);
		
		//store into the cache
		doctorCache.add(_doctor.getId(),_doctor);
		return new ResponseEntity<Doctor>(updatedDoctor, HttpStatus.OK);
	}

	/**
	 * Updates the Doctor's registration as rejected in Mongo DB based on the inputs provided by approver
	 * @param doctor
	 * @return
	 */
	@PutMapping("/{doctorId}/reject")
	public ResponseEntity<Doctor> rejectDoctor(@PathVariable String doctorId, @RequestBody Doctor doctor)
			throws ParseException {
		Doctor _doctor = doctorService.getDoctorById(doctorId);
		if (_doctor == null)
			throw new NoSuchElementException();
		_doctor.setApprovedBy(doctor.getApprovedBy());
		_doctor.setApproverComments(doctor.getApproverComments());
		_doctor.setStatus(rejectStatus);
		Date date = new Date();
		_doctor.setVerificationDate(convertToString(date));

		Doctor updatedDoctor = doctorService.saveDoctor(_doctor);
		
		//store into the cache
		doctorCache.add(_doctor.getId(),_doctor);
		return new ResponseEntity<Doctor>(updatedDoctor, HttpStatus.OK);
	}
	
	/**
	 * Returns list of all Doctor's from DB sorted in Rating (Descending order) with limit 20
	 * @param doctor
	 * @return
	 */
	@GetMapping
	public ResponseEntity<List<Doctor>> getDoctors(@RequestParam Map<String, String> allParams) {
		List<Doctor> _doctors = null;

		if (allParams.size() == 0)
			_doctors = doctorService.getAllDoctors();
		else if (allParams.size() == 1) {
			if (allParams.containsKey("status"))
				_doctors = doctorService.getDoctorsByStatus(allParams.get("status"));
			else if (allParams.containsKey("speciality"))
				_doctors = doctorService.getDoctorsBySpeciality(allParams.get("speciality"));
		} else if (allParams.size() == 2 && allParams.containsKey("status") && allParams.containsKey("speciality")) {
			_doctors = doctorService.getDoctorsByStatusAndSpeciality(allParams.get("status"),
					allParams.get("speciality"));
		}

		return new ResponseEntity<List<Doctor>>(_doctors, HttpStatus.OK);
	}
	
	/**
	 * Uploads the files checked in by user in to the S3 bucket
	 * @param doctor
	 * @return
	 */
	@PostMapping(value={"{doctorId}/documents", "{doctorId}/document"})
	public ResponseEntity<String> uploadFiles(@PathVariable("doctorId") String doctorId,
			@RequestParam MultipartFile[] files) throws IOException {
		for (MultipartFile file : files) {
			doctorService.uploadFiles(doctorId, file);
		}
		return new ResponseEntity<String>("File(s) uploaded Successfully", HttpStatus.OK);
	}
	
	/**
	 * Returns the file names of the files checked in by user in to the S3 bucket
	 * @param doctor
	 * @return
	 */
	@GetMapping("{doctorId}/documents/metadata")
	public ResponseEntity<String[]> getS3FileNames(@PathVariable("doctorId") String doctorId) {
		String[] files = doctorService.getFileNames(doctorId);
		return new ResponseEntity<String[]>(files, HttpStatus.OK);
	}

	/**
	 * This method handles exception when invalid input is provided when creating new Doctor
	 * 
	 * @param
	 * @return ErrorModel
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorModel handleInValidInputException() {
		ErrorModel errorModel = new ErrorModel();
		String[] errorFields = errorField.split(", ");
		errorModel.setErrorMessage(eInvalidInput);
		errorModel.setErrorCode(eInavlidInputCode);
		errorModel.setErrorFields(errorFields);
		return errorModel;
	}

	/**
	 * This method handles exception when invalid doctor id is provided as input during approval or rejection
	 * 
	 * @param
	 * @return ErrorModel
	 */
	@ExceptionHandler(NoSuchElementException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorModel handleRequestedResourceNotFoundException() {
		ErrorModel errorModel = new ErrorModel();
		errorModel.setErrorMessage(eResourceNotAvailable);
		errorModel.setErrorCode(eResourceNotFoundCode);
		errorModel.setErrorFields(null);
		return errorModel;
	}

	public String convertToString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}

	public String convertDateFormat(String strDate) throws ParseException {
		SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = inputFormat.parse(strDate);
		return outputFormat.format(date);
	}

}
