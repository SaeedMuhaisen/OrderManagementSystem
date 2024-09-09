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
import com.OrderManagementSystem.Models.Authentication.SellerRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

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
		var seller = SellerRegisterRequest.builder().email("seller@w.cn").password("123123").storeName("Store 1").firstname("seller").lastname("1").build();
		var seller2 = SellerRegisterRequest.builder().email("seller2@w.cn").password("123123").storeName("Store 2").firstname("seller").lastname("1").build();
		var buyer = RegisterRequest.builder().email("buyer@w.cn").password("123123").firstname("buyer").lastname("1").build();


		authenticationController.registerSeller(seller);
		authenticationController.registerSeller(seller2);
		authenticationController.registerCustomer(buyer);


		var storeAdmin=userRepository.findByEmail("seller@w.cn");
		var storeAdmin2=userRepository.findByEmail("seller2@w.cn");

		var store= storeRepository.findByEmployees_User(storeAdmin.get());
		var store2= storeRepository.findByEmployees_User(storeAdmin2.get());

		for(int i=1;i<=20;i++){
		var product = Product.builder()
				.id(UUID.randomUUID())
				.availableQuantity(100)
				.amountSold(0)
				.amountReturned(0)
				.visible(true)
				.created_t(Instant.now())
				.price(100.0)
				.description("First Product ")
				.name("Product "+i)
				.store(store.get()).build();

			var product2 = Product.builder()
					.id(UUID.randomUUID())
					.availableQuantity(100)
					.amountSold(0)
					.amountReturned(0)
					.visible(true)
					.created_t(Instant.now())
					.price(100.0)
					.description("First Product ")
					.name("Product "+i)
					.store(store2.get()).build();

			store.get().getProducts().add(product);
			store2.get().getProducts().add(product);
			storeRepository.save(store.get());
			storeRepository.save(store2.get());
			productRepository.save(product);
			productRepository.save(product2);
		}

	}
}