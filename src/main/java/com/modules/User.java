package com.modules;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.db.DBConnection;

public class User {

    public static boolean registerUser(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {

            try (PreparedStatement checkStmt = conn.prepareStatement(
                "SELECT id FROM users WHERE username = ?")) {
                checkStmt.setString(1, username);
                if (checkStmt.executeQuery().next()) {
                    System.out.println("User already exists: " + username);
                    return false;
                }
            }

            // ✅ role = 'USER' by default insert hoga
            try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users(username, password, email, role) VALUES(?, ?, ?, 'USER')")) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, username + "@quizapp.com");
                int rows = stmt.executeUpdate();
                System.out.println("User registered: " + username + " (rows: " + rows + ")");
                return rows > 0;
            }

        } catch (SQLException e) {
            System.out.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ✅ boolean ki jagah role String return karo
    public static String loginUser(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT role FROM users WHERE username = ? AND password = ?")) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String role = rs.getString("role");
                    System.out.println("Login SUCCESS: " + username + " role=" + role);
                    return role != null ? role : "USER";
                }
                System.out.println("Login FAILED: " + username);
                return null; // null matlab login fail
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}