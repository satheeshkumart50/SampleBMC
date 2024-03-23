package com.bmc.notificationservice.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates the KafkaConsumer object and configures the bootstrapServer to the KafkaConsumer object
 */
@Configuration
public class KafkaConsumerConfig {
    @Value("${kafka.server}")
    private String bootstrapServer;

    @Value("${group.id}")
    private String groupId;

    private Map<String,Object> getConfig() {
		Map<String,Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);
		return  configProps;
	}

	@Bean
	ConsumerFactory<String,String> consumerFactory(){
		return new DefaultKafkaConsumerFactory<String, String>(getConfig(),new StringDeserializer(),new StringDeserializer());
	}

	@Bean
	ConcurrentKafkaListenerContainerFactory<String, String> concurrentKafkaListenerContainerFactory(){
		ConcurrentKafkaListenerContainerFactory<String, String> factory = 
				new ConcurrentKafkaListenerContainerFactory<String, String>();
		factory.setConsumerFactory(consumerFactory());
		return  factory;
	}

}
