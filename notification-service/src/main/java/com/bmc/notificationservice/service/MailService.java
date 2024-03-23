package com.bmc.notificationservice.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.bmc.notificationservice.model.Appointment;
import com.bmc.notificationservice.model.Doctor;
import com.bmc.notificationservice.model.Prescription;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;

/**
 * This service class sends email using the AWS smtp server for the following condition
 * Doctor & User verification
 * Doctor Approval/Rejection
 * Appointment confirmation
 * Prescription update
 */
@Service
@RequiredArgsConstructor
public class MailService {

	private SesClient sesClient;
	private final FreeMarkerConfigurer configurer;
	@Value("${aws.ses.accessKey}")
	private String sesaAccessKey;
	@Value("${aws.ses.secretKey}")
	private String sesSecretKey;
	@Value("${aws.smtp.fromEmail}")
	private String fromEmail;
	@Value("${aws.smtp.host}")
	private String smtp;
	@Value("${aws.smtp.accessKey}")
	private String accessKey;
	@Value("${aws.smtp.secretKey}")
	private String secretKey;
	@Value("${mail.subject.registration}")
	private String registrationSubject;
	@Value("${mail.subject.prescription}")
	private String prescriptionSubject;
	@Value("${mail.subject.appointment}")
	private String appointmentSubject;


	@PostConstruct
	public void init(){
		StaticCredentialsProvider staticCredentialsProvider = StaticCredentialsProvider
				.create(AwsBasicCredentials.create(sesaAccessKey,sesSecretKey));
		sesClient = SesClient.builder()
				.credentialsProvider(staticCredentialsProvider)
				.region(Region.US_EAST_1)
				.build();
	}

	/*
	 * This method sends verification email to Doctor and User email id
	 */
	public void verifyEmail(String emailId){
		sesClient.verifyEmailAddress(req->req.emailAddress(emailId));
	}

	/*
	 * This method sends email on Doctor's approval/rejection
	 */
	public void sendDoctorRegistrationEmail(Doctor doctor) throws IOException, TemplateException, MessagingException {
		Map<String,Doctor> templateModelMap = new HashMap<>();
		templateModelMap.put("Doctor",doctor);
		Template freeMarkerTemplate = configurer.getConfiguration().getTemplate("doctorapproval.ftl");
		String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate,templateModelMap);
		sendSimpleMessage(doctor.getEmailId(),registrationSubject,htmlBody);
	}

	/*
	 * This method sends email for appointment confirmation
	 */
	public void sendAppointmentConfirmationEmail(Appointment appointment) throws MessagingException, IOException, TemplateException {
		Map<String,Appointment> templateModelMap = new HashMap<>();
		try {
			Timestamp ts=new Timestamp(Long.parseLong(appointment.getAppointmentDate()));  
			Date date=new Date(ts.getTime());
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");  
			appointment.setAppointmentDate(formatter.format(date));
		} catch (Exception e) {
			e.printStackTrace();
		}
		templateModelMap.put("Appointment",appointment);
		Template freeMarkerTemplate = configurer.getConfiguration().getTemplate("appointmentConfirmation.ftl");
		String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate,templateModelMap);
		sendSimpleMessage(appointment.getUserEmailId(),appointmentSubject,htmlBody);
	}

	/*
	 * This method sends email for Prescription update by Doctor
	 */
	public void sendPrescriptionEmail(Prescription prescription) throws IOException, TemplateException, MessagingException {
		Map<String,Prescription> templateModelMap = new HashMap<>();
		try {
			SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
			Date date = inputFormat.parse(prescription.getAppointmentDate());
			prescription.setAppointmentDate(outputFormat.format(date)); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		templateModelMap.put("Prescription",prescription);
		Template freeMarkerTemplate = configurer.getConfiguration().getTemplate("prescription.ftl");
		String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freeMarkerTemplate,templateModelMap);
		sendSimpleMessage(prescription.getUserEmailId(),prescriptionSubject+" "+prescription.getAppointmentDate()
		,htmlBody);
	}
	
	private void sendSimpleMessage(String toEmail, String subject, String body) throws MessagingException {
		Properties props = System.getProperties();
		props.put("mail.transport.protocol","smtp");
		props.put("mail.smtp.port",587);
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.auth","true");
		javax.mail.Session session = javax.mail.Session.getDefaultInstance(props);
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(fromEmail);
		msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
		msg.setSubject(subject);
		msg.setContent(body,"text/html");
		Transport transport = session.getTransport();
		try {
			transport.connect(smtp, accessKey,
					secretKey);
			transport.sendMessage(msg, msg.getAllRecipients());
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally {
			transport.close();
		}
	}
}
