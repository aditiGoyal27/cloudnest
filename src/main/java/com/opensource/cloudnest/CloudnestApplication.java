package com.opensource.cloudnest;

import com.opensource.cloudnest.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.HashSet;


@SpringBootApplication(scanBasePackages = "com.opensource.cloudnest")
@EnableJpaRepositories(basePackages = "com.opensource.cloudnest.repository")
@EnableJpaAuditing
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.opensource.cloudnest"})
public class CloudnestApplication  {



	@Autowired
	private RoleService roleService;

	public static void main(String[] args) {
		SpringApplication.run(CloudnestApplication.class, args);
	}


//
//	@Override
//	public void run(String... args) throws Exception {
//		System.out.println("Application has started, running one-time code!");
//
//
//		assignPermissions(
//		);
//		// Your startup code here
//	}


//	private void assignPermissions(){
//
//		role
//		roleService.addPermissionToRole()
//
//
//
//	}

}
