package com.sampledashboard1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class SampleDashboard1Application {

	public static void main(String[] args) {
		SpringApplication.run(SampleDashboard1Application.class, args);
	}

	@GetMapping("hello")
	public String welcome() {
		return "Hello from the DNJ app.";
	}
}
