package com.agms.zone_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ZoneServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZoneServiceApplication.class, args);
	}

}
