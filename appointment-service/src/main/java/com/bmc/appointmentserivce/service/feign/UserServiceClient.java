package com.bmc.appointmentserivce.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.bmc.appointmentserivce.model.User;

/**
 * Feign Client interface to reach out to User services
 *
 */
@FeignClient(name = "user-service",url="${gateway.service.url}")
public interface UserServiceClient {
    
	@GetMapping("/users/{userId}")
	public User getUser(@RequestHeader("Authorization") String token,@PathVariable String userId);
}
