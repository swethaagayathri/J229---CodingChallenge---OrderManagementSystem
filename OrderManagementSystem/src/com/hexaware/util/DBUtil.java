package com.hexaware.util;

import java.sql.*;

public class DBUtil {
	static Connection con;

	public static Connection getDBConn() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/oms", "root", "root");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	public static void main(String args[]) {
		System.out.println(getDBConn());
	}

}
