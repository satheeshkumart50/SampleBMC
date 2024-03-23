package com.bmc.doctorservice.service;

import java.io.IOException;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerService {
	
	void listen(ConsumerRecord<String, String> record) throws IOException;

}
