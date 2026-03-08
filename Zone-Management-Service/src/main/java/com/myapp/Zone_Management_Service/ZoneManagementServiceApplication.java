package com.myapp.Zone_Management_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ZoneManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZoneManagementServiceApplication.class, args);
	}

}
