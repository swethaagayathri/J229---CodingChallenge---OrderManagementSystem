package com.hexaware.dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hexaware.entity.Clothing;
import com.hexaware.entity.Electronics;
import com.hexaware.entity.Product;
import com.hexaware.entity.User;
import com.hexaware.exception.OrderNotFoundException;
import com.hexaware.exception.UserNotFoundException;
import com.hexaware.util.DBUtil;

public class OrderProcessor implements IOrderManagementRepository {
	private static final Connection conn = DBUtil.getDBConn();
	private static int orderId = getLastUsedOrderIdFromDB();;

	@Override
	public void createUser(User user) {
		try {
			String query = "INSERT INTO USERS (user_id, username, password, role) VALUES (?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setLong(1, user.getUserId());
			ps.setString(2, user.getUsername());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getRole());
			int numberOfRows = ps.executeUpdate();
			System.out.println(numberOfRows + " row inserted successfully in User table!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void createProduct(User user, Product product) {

		try {
			if (!userExists(user.getUserId())) {
				createUser(user);
			}
			String query = "INSERT INTO Product (product_id, product_name, description, price, quantity_in_stock, type, brand, warranty_period, size, color) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, product.getProductId());
			pstmt.setString(2, product.getProductName());
			pstmt.setString(3, product.getDescription());
			pstmt.setDouble(4, product.getPrice());
			pstmt.setInt(5, product.getQuantityInStock());
			pstmt.setString(6, product.getType());
			if (product.getType().equals("Electronics")) {
				pstmt.setString(7, ((Electronics) product).getBrand());
				pstmt.setInt(8, ((Electronics) product).getWarrantyPeriod());
				pstmt.setNull(9, java.sql.Types.VARCHAR);
				pstmt.setNull(10, java.sql.Types.VARCHAR);
			} else {
				pstmt.setNull(7, java.sql.Types.VARCHAR);
				pstmt.setNull(8, java.sql.Types.INTEGER);
				pstmt.setString(9, ((Clothing) product).getSize());
				pstmt.setString(10, ((Clothing) product).getColor());
			}
			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Product created successfully.");
			} else {
				System.out.println("Failed to create product.");
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	@Override
	public void createOrder(User user, List<Product> products) {

		try {
			if (!userExists(user.getUserId())) {
				createUser(user);
			}
			int newOrderId = orderId++;
			for (Product product : products) {
				addProductToOrder(newOrderId, user.getUserId(), product.getProductId());
			}

			System.out.println("Order created successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void addProductToOrder(int orderId, int userId, int productId) throws SQLException {
		String query = "INSERT INTO Orders (order_id, user_id, product_id) VALUES (?, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, orderId);
		pstmt.setInt(2, userId);
		pstmt.setInt(3, productId);
		pstmt.executeUpdate();

	}

	@Override
	public void cancelOrder(int userId, int orderId) throws UserNotFoundException, OrderNotFoundException {

		try {
			if (!userExists(userId)) {
				throw new UserNotFoundException("User with ID " + userId + " not found.");
			}

			if (!orderExists(orderId)) {
				throw new OrderNotFoundException("Order with ID " + orderId + " not found.");
			}
			String query = "DELETE FROM orders WHERE order_id = ?";
			PreparedStatement statement = conn.prepareStatement(query);
			statement.setInt(1, orderId);
			statement.executeUpdate();
			System.out.println("Order canceled successfully.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Product> getAllProducts() {
		List<Product> productList = new ArrayList<>();
		try {
			String query = "SELECT * FROM Product";
			PreparedStatement pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int productId = rs.getInt("product_id");
				String productName = rs.getString("product_name");
				String description = rs.getString("description");
				double price = rs.getDouble("price");
				int quantityInStock = rs.getInt("quantity_in_stock");
				String type = rs.getString("type");

				Product product;
				if ("Electronics".equals(type)) {
					String brand = rs.getString("brand");
					int warrantyPeriod = rs.getInt("warranty_period");
					product = new Electronics(productId, productName, description, price, quantityInStock, type, brand,
							warrantyPeriod);
				} else {
					String size = rs.getString("size");
					String color = rs.getString("color");
					product = new Clothing(productId, productName, description, price, quantityInStock, type, size,
							color);
				}

				productList.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return productList;
	}

	public List<Product> getOrderByUser(User user) throws UserNotFoundException {
		List<Product> orderedProducts = new ArrayList<>();

		try {
			if (!userExists(user.getUserId())) {
				throw new UserNotFoundException("User with ID " + user.getUserId() + " not found");
			}
			String query = "SELECT product_id FROM Orders WHERE user_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, user.getUserId());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int productId = rs.getInt("product_id");
				Product product = getProductById(productId);
				orderedProducts.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderedProducts;
	}

	private Product getProductById(int productId) {
		Product product = null;
		try {
			String query = "SELECT * FROM Product WHERE product_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, productId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String productName = rs.getString("product_name");
				String description = rs.getString("description");
				double price = rs.getDouble("price");
				int quantityInStock = rs.getInt("quantity_in_stock");
				String type = rs.getString("type");

				if ("Electronics".equals(type)) {
					String brand = rs.getString("brand");
					int warrantyPeriod = rs.getInt("warranty_period");
					product = new Electronics(productId, productName, description, price, quantityInStock, type, brand,
							warrantyPeriod);
				} else {
					String size = rs.getString("size");
					String color = rs.getString("color");
					product = new Clothing(productId, productName, description, price, quantityInStock, type, size,
							color);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}

	private boolean orderExists(int orderId) throws SQLException {
		String query = "SELECT * FROM orders WHERE order_id = ?";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setInt(1, orderId);
		ResultSet resultSet = statement.executeQuery();
		return resultSet.next();
	}

	private boolean userExists(int userId) throws SQLException {
		String query = "SELECT * FROM users WHERE user_id = ?";
		PreparedStatement statement = conn.prepareStatement(query);
		statement.setInt(1, userId);
		ResultSet rs = statement.executeQuery();
		return rs.next();

	}
	public static int getLastUsedOrderIdFromDB() {
		int orderId = 0;
		try {
			String query = "SELECT MAX(Order_Id) AS LastOrderId FROM Orders";
			PreparedStatement pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				orderId = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Math.max(10000, orderId + 1);
	}

}
