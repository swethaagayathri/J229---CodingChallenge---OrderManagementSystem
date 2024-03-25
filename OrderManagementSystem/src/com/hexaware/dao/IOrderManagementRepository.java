package com.hexaware.dao;

import java.util.List;
import com.hexaware.entity.User;
import com.hexaware.entity.Product;
import com.hexaware.exception.UserNotFoundException;
import com.hexaware.exception.OrderNotFoundException;

public interface IOrderManagementRepository {

	void createOrder(User user, List<Product> products);

	void cancelOrder(int userId, int orderId) throws UserNotFoundException, OrderNotFoundException;

	void createProduct(User user, Product product);

	void createUser(User user);

	List<Product> getAllProducts();

	List<Product> getOrderByUser(User user) throws UserNotFoundException;
}
