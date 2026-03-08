package com.myapp.Crop_Inventory_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CropInventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CropInventoryServiceApplication.class, args);
	}

}
