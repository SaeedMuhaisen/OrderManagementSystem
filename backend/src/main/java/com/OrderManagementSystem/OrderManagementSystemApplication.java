package com.OrderManagementSystem;

import com.OrderManagementSystem.CSR.Controllers.AuthenticationController;
import com.OrderManagementSystem.CSR.Repositories.ProductRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.Entities.Product;
import com.OrderManagementSystem.Entities.enums.Role;
import com.OrderManagementSystem.Models.Authentication.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.time.Instant;

@SpringBootApplication
public class OrderManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderManagementSystemApplication.class, args);
	}

}
@Component
@RequiredArgsConstructor
 class CommandLineAppStartupRunner implements CommandLineRunner {

	private final AuthenticationController authenticationController;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	@Override
	public void run(String...args) throws Exception {
		if(userRepository.findByEmail("seller@w.cn").isPresent()){
			return;
		}
		var seller = RegisterRequest.builder().email("seller@w.cn").password("123123").firstname("seller").lastname("1").build();
		var seller2 = RegisterRequest.builder().email("seller2@w.cn").password("123123").firstname("seller2").lastname("2").build();
		var buyer = RegisterRequest.builder().email("buyer@w.cn").password("123123").firstname("buyer").lastname("1").build();
		var admin = RegisterRequest.builder().email("admin@w.cn").password("123123").firstname("admin").lastname("1").build();

		authenticationController.register(seller);
		authenticationController.register(seller2);
		authenticationController.register(buyer);
		authenticationController.register(admin);

		var eSeller= userRepository.findByEmail("seller@w.cn");
		var eSeller2= userRepository.findByEmail("seller2@w.cn");
		var eBuyer= userRepository.findByEmail("buyer@w.cn");
		var eAdmin= userRepository.findByEmail("admin@w.cn");

		eSeller.get().setRole(Role.SELLER);
		eSeller2.get().setRole(Role.SELLER);
		eAdmin.get().setRole(Role.ADMIN);
		eBuyer.get().setRole(Role.BUYER);


		var p1s1= Product.builder()
				.user(eSeller.get())
				.name("P1")
				.description("P1 S1")
				.price(100.0)
				.created_t(Instant.now())
				.availableQuantity(100)
				.amountSold(0)
				.amountReturned(0)
				.visible(true)
				.build();
		var p2s1= Product.builder()
				.user(eSeller.get())
				.name("P2")
				.description("P2 S1")
				.price(100.0)
				.created_t(Instant.now())
				.availableQuantity(100)
				.amountSold(0)
				.amountReturned(0)
				.visible(true)
				.build();
		var p1s2= Product.builder()
				.user(eSeller2.get())
				.name("P1")
				.description("P1 S2")
				.price(100.0)
				.created_t(Instant.now())
				.availableQuantity(100)
				.amountSold(0)
				.amountReturned(0)
				.visible(true)
				.build();



		userRepository.save(eSeller.get());
		userRepository.save(eSeller2.get());
		userRepository.save(eAdmin.get());
		userRepository.save(eBuyer.get());
		productRepository.save(p1s1);
		productRepository.save(p2s1);
		productRepository.save(p1s2);
	}
}