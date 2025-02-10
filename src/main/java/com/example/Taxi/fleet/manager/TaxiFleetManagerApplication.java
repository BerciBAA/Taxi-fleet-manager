package com.example.Taxi.fleet.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TaxiFleetManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaxiFleetManagerApplication.class, args);
	}

}
