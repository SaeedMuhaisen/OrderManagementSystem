package com.OrderManagementSystem;

import com.OrderManagementSystem.CSR.Controllers.AuthenticationController;
import com.OrderManagementSystem.CSR.Repositories.ProductRepository;
import com.OrderManagementSystem.CSR.Repositories.StoreRepository;
import com.OrderManagementSystem.CSR.Repositories.UserRepository;
import com.OrderManagementSystem.Entities.Product;
import com.OrderManagementSystem.Entities.Store;
import com.OrderManagementSystem.Entities.StoreEmployee;
import com.OrderManagementSystem.Entities.enums.EmployeeRole;
import com.OrderManagementSystem.Entities.enums.Role;
import com.OrderManagementSystem.Models.Authentication.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

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
	private final StoreRepository storeRepository;
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
		var buyerUpdated=userRepository.findByEmail("buyer@w.cn");
		buyerUpdated.get().setRole(Role.BUYER);
		userRepository.save(buyerUpdated.get());

		var storeAdmin=userRepository.findByEmail("seller@w.cn");

		var store = Store.builder().name("Store 1").build();
		var storeEmployee= StoreEmployee.builder().store(store).employeeRole(EmployeeRole.ADMIN).user(storeAdmin.get()).build();

		store.setEmployees(List.of(storeEmployee));

		storeRepository.save(store);

		for(int i=1;i<=20;i++){
		var product = Product.builder()
				.availableQuantity(100)
				.amountSold(0)
				.amountReturned(0)
				.visible(true)
				.created_t(Instant.now())
				.price(100.0)
				.description("First Product ")
				.name("Product "+i)
				.store(store).build();
			productRepository.save(product);
		}


	}
}