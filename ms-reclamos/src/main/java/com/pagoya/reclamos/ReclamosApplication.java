package com.pagoya.reclamos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ReclamosApplication {
	public static void main(String[] args) {
		SpringApplication.run(ReclamosApplication.class, args);
	}
}
