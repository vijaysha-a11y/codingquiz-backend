package com.auth;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.db.DBConnection;

public class UserAuth {
    public static boolean registerUser(String username, String password) throws Exception {
        String hashed = hashPassword(password);
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO users(username, password) VALUES(?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, hashed);
            return stmt.executeUpdate() > 0;
        }
    }

    public static boolean loginUser(String username, String password) throws Exception {
        String hashed = hashPassword(password);
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            stmt.setString(1, username);
            stmt.setString(2, hashed);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    private static String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
