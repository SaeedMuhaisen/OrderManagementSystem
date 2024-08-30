package com.OrderManagementSystem;

import com.OrderManagementSystem.CSR.Controllers.AuthenticationController;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.Entities.Role;
import com.OrderManagementSystem.Entities.User;
import com.OrderManagementSystem.Entities.enums.UserStatus;
import com.OrderManagementSystem.Models.Authentication.AuthenticationRequest;
import com.OrderManagementSystem.Models.Authentication.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class OrderManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementSystemApplication.class, args);
	}

}
@Component
 class CommandLineAppStartupRunner implements CommandLineRunner {
	@Autowired
	AuthenticationController authenticationController;
	@Autowired
	UserRepository userRepository;
	@Override
	public void run(String...args) throws Exception {
		if(userRepository.findByEmail("seller@w.cn").isPresent()){
			return;
		}
		var seller = RegisterRequest.builder().email("seller@w.cn").password("123123").firstname("seller").lastname("1").build();
		var buyer = RegisterRequest.builder().email("buyer@w.cn").password("123123").firstname("buyer").lastname("1").build();
		var admin = RegisterRequest.builder().email("admin@w.cn").password("123123").firstname("admin").lastname("1").build();
		authenticationController.register(seller);
		authenticationController.register(buyer);
		authenticationController.register(admin);

		var eSeller= userRepository.findByEmail("seller@w.cn");
		var eBuyer= userRepository.findByEmail("buyer@w.cn");
		var eAdmin= userRepository.findByEmail("admin@w.cn");

		eSeller.get().setRole(Role.SELLER);
		eAdmin.get().setRole(Role.ADMIN);
		eBuyer.get().setRole(Role.BUYER);

		userRepository.save(eSeller.get());
		userRepository.save(eAdmin.get());
		userRepository.save(eBuyer.get());

	}
}