package com.bmc.appointmentserivce.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.bmc.appointmentserivce.model.Doctor;

/**
 * Feign Client interface to reach out to Doctor services
 *
 */
@FeignClient(name = "doctor-service",url="${gateway.service.url}")
public interface DoctorServiceClient {
	
	@GetMapping("/doctors/{doctorId}")
	public Doctor getDoctor(@RequestHeader("Authorization") String token, @PathVariable String doctorId);

}
