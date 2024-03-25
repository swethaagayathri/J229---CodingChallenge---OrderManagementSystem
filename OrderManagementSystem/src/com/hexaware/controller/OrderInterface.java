package com.hexaware.controller;

import com.hexaware.exception.OrderNotFoundException;
import com.hexaware.exception.UserNotFoundException;

public interface OrderInterface {

    void createUser();

    void createProduct();

    void createOrder();

    void cancelOrder() throws UserNotFoundException, OrderNotFoundException;

    void getAllProducts();

    void getOrdersByUser() throws UserNotFoundException;
}
