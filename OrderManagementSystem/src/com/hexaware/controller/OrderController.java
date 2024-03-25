package com.hexaware.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.hexaware.entity.*;
import com.hexaware.dao.*;
import com.hexaware.entity.User;
import com.hexaware.exception.OrderNotFoundException;
import com.hexaware.exception.UserNotFoundException;

public class OrderController implements OrderInterface{
	
	private final IOrderManagementRepository orderProcessor = new OrderProcessor();
	Scanner sc = new Scanner(System.in);

	public User getUserDetails() {
		System.out.println("Enter User Details");
		System.out.println("UserId: ");
		int userId = sc.nextInt();
		sc.nextLine();
		System.out.print("Username: ");
		String username = sc.nextLine();
		System.out.print("Password: ");
		String password = sc.nextLine();
		System.out.print("Role(Admin/User): ");
		String role = sc.nextLine();
		User newUser = new User(userId, username, password, role);
		return newUser;
	}

	public void createUser() {
		User user = getUserDetails();
		orderProcessor.createUser(user);
	}

	public void createProduct() {
		User user = getUserDetails();
		System.out.println("Enter product details:");
		System.out.print("Product ID: ");
		int productId = sc.nextInt();
		sc.nextLine();
		System.out.print("Product Name: ");
		String productName = sc.nextLine();
		System.out.print("Description: ");
		String description = sc.nextLine();
		System.out.print("Price: ");
		double price = sc.nextDouble();
		sc.nextLine();
		System.out.print("Quantity in Stock: ");
		int quantityInStock = sc.nextInt();
		sc.nextLine();
		System.out.print("Type (Electronics/Clothing): ");
		String type = sc.nextLine();
		Product product = null;
		if ("Electronics".equalsIgnoreCase(type)) {
			System.out.print("Brand: ");
			String brand = sc.nextLine();
			System.out.print("Warranty Period: ");
			int warrantyPeriod = sc.nextInt();
			product = new Electronics(productId, productName, description, price, quantityInStock, type, brand,
					warrantyPeriod);
		} else if ("Clothing".equalsIgnoreCase(type)) {
			System.out.print("Size: ");
			String size = sc.nextLine();
			System.out.print("Color: ");
			String color = sc.nextLine();
			product = new Clothing(productId, productName, description, price, quantityInStock, type, size, color);
		} else {
			System.out.println("Invalid product type. Please enter either 'Electronics' or 'Clothing'.");
			return;
		}

		orderProcessor.createProduct(user, product);

	}

	public void createOrder() {
		User user = getUserDetails();
		List<Product> products = new ArrayList<>();
		System.out.println("Enter product IDs for the order (enter -1 to stop):");
		int productId;
		while (true) {
			System.out.print("Product ID: ");
			productId = sc.nextInt();
			if (productId == -1) {
				break;
			}
			Product product = new Product(productId, "", "", 0.0, 0, "");
			products.add(product);
		}
		orderProcessor.createOrder(user, products);
	}

	public void cancelOrder() throws UserNotFoundException, OrderNotFoundException {
		System.out.print("Enter User ID: ");
		int userId = sc.nextInt();
		System.out.print("Enter Order ID: ");
		int orderId = sc.nextInt();
		orderProcessor.cancelOrder(userId, orderId);
	}

	public void getAllProducts() {
		List<Product> products = orderProcessor.getAllProducts();
		System.out.println("List of all products:");
		for (Product product : products) {
			System.out.println(product);
		}
	}

	public void getOrdersByUser() throws UserNotFoundException {
		System.out.print("Enter User ID: ");
		int userId = sc.nextInt();
		User user = new User(userId, "", "", "");
		List<Product> orderedProducts = orderProcessor.getOrderByUser(user);
		System.out.println("Products ordered by user " + userId + ": ");
		for (Product product : orderedProducts) {
			System.out.println(product);
		}
	}

}
