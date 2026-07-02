package com.controller;

import java.sql.*;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import com.db.DBConnection;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class LeaderboardController {

    @GetMapping("/leaderboard")
    public List<Map<String, Object>> getLeaderboard(
            @RequestParam(defaultValue = "average") String sortBy) {

        List<Map<String, Object>> leaderboard = new ArrayList<>();

        // averageScore ab percentage ke hisaab se calculate hota hai:
        // har result ka (score / us quiz ke total questions) * 100, fir un sab ka average
        String query =
            "SELECT r.username, COUNT(*) as attempts, " +
            "AVG(r.score * 100.0 / qc.total) as averageScore, " +
            "MAX(r.score) as bestScore " +
            "FROM results r " +
            "JOIN (SELECT quiz_id, COUNT(*) as total FROM questions GROUP BY quiz_id) qc " +
            "ON r.quiz_id = qc.quiz_id " +
            "GROUP BY r.username ";

        if ("average".equals(sortBy)) {
            query += "ORDER BY averageScore DESC";
        } else if ("best".equals(sortBy)) {
            query += "ORDER BY bestScore DESC";
        } else if ("attempts".equals(sortBy)) {
            query += "ORDER BY attempts DESC";
        } else {
            query += "ORDER BY averageScore DESC";
        }

        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            int rank = 1;

            while (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("rank", rank++);
                user.put("username", rs.getString("username"));
                user.put("attempts", rs.getInt("attempts"));
                user.put("averageScore", rs.getDouble("averageScore"));
                user.put("bestScore", rs.getInt("bestScore"));
                leaderboard.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return leaderboard;
    }
}