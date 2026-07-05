package com.quiz;

import java.sql.Connection;
import java.sql.SQLException;

public final class DBConnection {

	private DBConnection() {
	}

	public static Connection getConnection() throws SQLException {
		return com.db.DBConnection.getConnection();
	}
}
