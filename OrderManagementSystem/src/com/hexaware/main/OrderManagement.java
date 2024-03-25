package com.hexaware.main;
import com.hexaware.*;
import java.util.Scanner;
import com.hexaware.controller.OrderController;
import com.hexaware.controller.OrderInterface;
import com.hexaware.exception.OrderNotFoundException;
import com.hexaware.exception.UserNotFoundException;

public class OrderManagement {
	private static OrderInterface orderController = new OrderController();
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		boolean exit = false;
		System.out.println("Welcome to Order Management System!");
		while (!exit) {
			System.out.println("Select an option:");
			System.out.println("1. Create User");
			System.out.println("2. Create Product");
			System.out.println("3. Create Order");
			System.out.println("4. Cancel Order");
			System.out.println("5. Get All Products");
			System.out.println("6. Get Orders by User");
			System.out.println("7. Exit");
			int choice = scanner.nextInt();
			scanner.nextLine();

			try {
				switch (choice) {
				case 1:
					orderController.createUser();
					break;
				case 2:
					orderController.createProduct();
					break;
				case 3:
					orderController.createOrder();
					break;
				case 4:
					orderController.cancelOrder();
					break;
				case 5:
					orderController.getAllProducts();
					break;
				case 6:
					orderController.getOrdersByUser();
					break;
				case 7:
					exit = true;
					System.out.println("Exiting...");
					break;
				default:
					System.out.println("Invalid choice. Please enter a number between 1 and 7.");
					break;
				}
			} catch (UserNotFoundException | OrderNotFoundException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Thank you for using the Order Management System!");

	}
}
