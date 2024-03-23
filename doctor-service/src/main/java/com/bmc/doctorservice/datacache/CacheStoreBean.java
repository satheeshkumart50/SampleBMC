package com.bmc.doctorservice.datacache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bmc.doctorservice.model.Doctor;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheStoreBean {
    @Bean
    public CacheStore<Doctor> doctorCache(){
        return new CacheStore<Doctor>(120, TimeUnit.SECONDS);
    }
}