package com.user.user_management_system;

import com.user.user_management_system.office.model.IOfficeRepository;
import com.user.user_management_system.office.model.Office;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserManagementSystemApplication {


	public static void main(String[] args) {
		SpringApplication.run(
				UserManagementSystemApplication.class, args);


	}

}
