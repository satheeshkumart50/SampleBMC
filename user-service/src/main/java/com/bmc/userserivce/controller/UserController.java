package com.bmc.userserivce.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bmc.userserivce.model.User;
import com.bmc.userserivce.model.ErrorModel;
import com.bmc.userserivce.service.UserService;

/**
 * Contains the controller methods for all end-points related to User services
 *
 */
@RestController
@RequestMapping("/users")
public class UserController {

	private UserService userService;

	@Value("${error.user.resourceNotAvailable}")
	private String eResourceNotAvailable;
	@Value("${error.user.invalidInput}")
	private String eInvalidInput;
	@Value("${errorCode.user.resourceNotAvailable}")
	private String eResourceNotFoundCode;
	@Value("${errorCode.user.invalidInput}")
	private String eInavlidInputCode;
	@Value("${error.user.fields}")
	private String errorField;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * This method fetches the user details based on the ID provided as input
	 * @param userId
	 * @return
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<User> getUserbyId(@PathVariable String userId) {

		User _user = userService.getUserById(userId);
		if (_user == null)
			throw new NoSuchElementException();
		
		return new ResponseEntity<User>(_user, HttpStatus.OK);
	}

	/**
	 * This method adds new user to the Mongo DB
	 * @param user
	 * @return
	 * @throws ParseException
	 */
	@PostMapping
	public ResponseEntity<User> addNewUser(@RequestBody @Valid User user) throws ParseException {
	//	String dob = user.getDob();
	//	user.setDob(convertDateFormat(dob));
		Date date = new Date();
		user.setCreatedDate(convertToString(date));
		
		User _user = userService.saveUser(user);
		return new ResponseEntity<User>(_user, HttpStatus.OK);
	}

	/**
	 * Uploads the files checked in by user in to the S3 bucket
	 * @param doctor
	 * @return
	 */
	@PostMapping(value={"{userId}/documents", "{userId}/document"})
	public ResponseEntity<String> uploadFiles(@PathVariable("userId") String userId,
			@RequestParam MultipartFile[] files) throws IOException {
		for (MultipartFile file : files) {
			userService.uploadFiles(userId, file);
		}
		return new ResponseEntity<String>("File(s) uploaded Successfully", HttpStatus.OK);
	}

	/**
	 * Returns the file names of the files checked in by user in to the S3 bucket
	 * @param doctor
	 * @return
	 */
	@GetMapping("{userId}/documents/metadata")
	public ResponseEntity<String[]> getS3FileNames(@PathVariable("userId") String userId) {
		String[] files = userService.getFileNames(userId);
		return new ResponseEntity<String[]>(files, HttpStatus.OK);
	}

	/**
	 * This method handles exception when payment is made for the unbooked rooms
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
	 * This method handles exception when payment is made for the unbooked rooms
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

	public static String convertToString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}

	public static String convertDateFormat(String strDate) throws ParseException {
		SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = inputFormat.parse(strDate);
		return outputFormat.format(date);
	}

}
