package com.bmc.notificationservice.service;

import java.io.IOException;

import javax.mail.MessagingException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import freemarker.template.TemplateException;

public interface ConsumerService {
	
	void listen(ConsumerRecord<String, String> record) throws JsonMappingException, JsonProcessingException, IOException, TemplateException, MessagingException;

}
