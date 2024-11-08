package com.opensource.cloudnest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.opensource.cloudnest.repository")
@EnableJpaAuditing
@ComponentScan(basePackages = {"com.opensource.cloudnest"})
public class CloudnestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudnestApplication.class, args);
	}

}
