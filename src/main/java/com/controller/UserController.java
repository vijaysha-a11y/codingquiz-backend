package com.controller;

import java.sql.*;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import com.db.DBConnection;

@RestController
@RequestMapping("/api")
// @CrossOrigin(origins = "http://localhost:3000")
<<<<<<< HEAD
@CrossOrigin(origins = "https://codingquiz-frontend.onrender.com")
=======
// @CrossOrigin(origins = "https://codingquiz-frontend.onrender.com")
>>>>>>> 3e55ba82dcadcaf7bd728494d80568cac9a2fc6d
public class UserController {

    @GetMapping("/user-stats")
    public Map<String, Object> getUserStats(@RequestParam String username) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("username", username);
        stats.put("totalQuizzesTaken", 0);
        stats.put("averageScore", 0);
        stats.put("bestScore", 0);
        stats.put("rank", 0);

        try (Connection conn = DBConnection.getConnection()) {
            // Basic stats - averageScore ab percentage ke hisaab se calculate hota hai
            // (score / us quiz ke total questions) * 100, fir un percentages ka average
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(*) as attemptCount, " +
                "AVG(r.score * 100.0 / qc.questionTotal) as avgPercentage, " +
                "MAX(r.score) as bestRawScore " +
                "FROM results r " +
                "JOIN (SELECT quiz_id, COUNT(*) as questionTotal FROM questions GROUP BY quiz_id) qc " +
                "ON r.quiz_id = qc.quiz_id " +
                "WHERE r.username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int attemptCount = rs.getInt("attemptCount");
                double avgPercentage = rs.getDouble("avgPercentage");
                int bestRawScore = rs.getInt("bestRawScore");

                System.out.println("DEBUG /user-stats -> attemptCount=" + attemptCount
                        + " avgPercentage=" + avgPercentage + " bestRawScore=" + bestRawScore);

                stats.put("totalQuizzesTaken", attemptCount);
                stats.put("averageScore", avgPercentage);
                stats.put("bestScore", bestRawScore);
            }

            // Rank calculation - bhi ab percentage-based average use karta hai
            PreparedStatement rankStmt = conn.prepareStatement(
                "SELECT COUNT(*) as user_rank FROM (" +
                "  SELECT r.username, AVG(r.score * 100.0 / qc.total) as avg " +
                "  FROM results r " +
                "  JOIN (SELECT quiz_id, COUNT(*) as total FROM questions GROUP BY quiz_id) qc " +
                "  ON r.quiz_id = qc.quiz_id " +
                "  GROUP BY r.username" +
                ") t " +
                "WHERE avg > (" +
                "  SELECT AVG(r2.score * 100.0 / qc2.total) " +
                "  FROM results r2 " +
                "  JOIN (SELECT quiz_id, COUNT(*) as total FROM questions GROUP BY quiz_id) qc2 " +
                "  ON r2.quiz_id = qc2.quiz_id " +
                "  WHERE r2.username = ?" +
                ")");
            rankStmt.setString(1, username);
            ResultSet rankRs = rankStmt.executeQuery();
            if (rankRs.next()) {
                stats.put("rank", rankRs.getInt("user_rank") + 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }

    @GetMapping("/recent-quizzes")
    public List<Map<String, Object>> getRecentQuizzes(@RequestParam String username) {
        List<Map<String, Object>> quizzes = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            // totalQuestions ab actual questions table se aata hai, hardcoded 10 nahi
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT r.quiz_id, q.title, r.score, r.attempted_at, qc.total as totalQuestions " +
                "FROM results r " +
                "JOIN quizzes q ON r.quiz_id = q.id " +
                "JOIN (SELECT quiz_id, COUNT(*) as total FROM questions GROUP BY quiz_id) qc " +
                "ON r.quiz_id = qc.quiz_id " +
                "WHERE r.username = ? ORDER BY r.attempted_at DESC LIMIT 5");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> quiz = new HashMap<>();
                quiz.put("id", rs.getInt("quiz_id"));
                quiz.put("title", rs.getString("title"));
                quiz.put("score", rs.getInt("score"));
                quiz.put("totalQuestions", rs.getInt("totalQuestions"));
                quiz.put("date", rs.getTimestamp("attempted_at"));
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return quizzes;
    }

    @GetMapping("/profile")
    public Map<String, Object> getProfile(@RequestParam String username) {
        Map<String, Object> profile = new HashMap<>();
        profile.put("username", username);

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT COUNT(*) as attemptCount, " +
                "AVG(r.score * 100.0 / qc.questionTotal) as avgPercentage, " +
                "MAX(r.score) as bestRawScore " +
                "FROM results r " +
                "JOIN (SELECT quiz_id, COUNT(*) as questionTotal FROM questions GROUP BY quiz_id) qc " +
                "ON r.quiz_id = qc.quiz_id " +
                "WHERE r.username = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                profile.put("quizzesTaken", rs.getInt("attemptCount"));
                profile.put("averageScore", rs.getDouble("avgPercentage"));
                profile.put("bestScore", rs.getInt("bestRawScore"));
            }

            // Placeholder values — replace with actual user profile table if needed
            profile.put("email", "user@example.com");
            profile.put("fullName", username);
            profile.put("bio", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return profile;
    }

    @PutMapping("/profile")
    public Map<String, Object> updateProfile(@RequestBody ProfileRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("username", request.getUsername());
        response.put("email", request.getEmail());
        response.put("fullName", request.getFullName());
        response.put("bio", request.getBio());
        return response;
    }
}

class ProfileRequest {
    private String username;
    private String email;
    private String fullName;
    private String bio;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
}