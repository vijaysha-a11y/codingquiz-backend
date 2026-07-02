package com.quiz;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Leaderboard {
    public static void showLeaderboard() throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT username, AVG(score) as avg_score FROM results GROUP BY username ORDER BY avg_score DESC");

            System.out.println("Leaderboard:");
            while (rs.next()) {
                System.out.println(rs.getString("username") + " - " + rs.getDouble("avg_score"));
            }
        }
    }
}
