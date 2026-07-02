package com.controller;

import java.sql.*;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import com.db.DBConnection;

@RestController
@RequestMapping("/api")
// @CrossOrigin(origins = "http://localhost:3000")

// ✅ Update CORS origin to your live frontend
// @CrossOrigin(origins = "https://codingquiz-frontend.onrender.com")

public class QuizController {

    // ================= GET ALL QUIZZES =================
    @GetMapping("/quizzes")
    public List<Map<String, Object>> getAllQuizzes() {
        List<Map<String, Object>> quizzes = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT id, title, description, category, difficulty, duration FROM quizzes");
            while (rs.next()) {
                Map<String, Object> quiz = new HashMap<>();
                int quizId = rs.getInt("id");
                quiz.put("id", quizId);
                quiz.put("title", rs.getString("title"));
                quiz.put("description", rs.getString("description"));
                quiz.put("category", rs.getString("category"));
                quiz.put("difficulty", rs.getString("difficulty"));
                quiz.put("duration", rs.getInt("duration"));

                PreparedStatement countStmt = conn.prepareStatement(
                        "SELECT COUNT(*) AS total FROM questions WHERE quiz_id = ?");
                countStmt.setInt(1, quizId);
                ResultSet countRs = countStmt.executeQuery();
                int totalQuestions = 0;
                if (countRs.next()) {
                    totalQuestions = countRs.getInt("total");
                }
                quiz.put("totalQuestions", totalQuestions);
                quizzes.add(quiz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    // ================= GET QUIZ BY ID =================
    @GetMapping("/quiz/{id}")
    public Map<String, Object> getQuizById(@PathVariable int id) {
        Map<String, Object> quiz = new HashMap<>();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM quizzes WHERE id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                quiz.put("id", rs.getInt("id"));
                quiz.put("title", rs.getString("title"));
                quiz.put("description", rs.getString("description"));
                quiz.put("category", rs.getString("category"));
                quiz.put("difficulty", rs.getString("difficulty"));
                quiz.put("duration", rs.getInt("duration"));

                List<Map<String, Object>> questions = new ArrayList<>();
                PreparedStatement qStmt = conn.prepareStatement("SELECT * FROM questions WHERE quiz_id = ?");
                qStmt.setInt(1, id);
                ResultSet qRs = qStmt.executeQuery();

                while (qRs.next()) {
                    Map<String, Object> q = new HashMap<>();
                    q.put("id", qRs.getInt("id"));
                    q.put("questionText", qRs.getString("question"));
                    q.put("paragraph", qRs.getString("paragraph"));
                    q.put("optionA", qRs.getString("optionA"));
                    q.put("optionB", qRs.getString("optionB"));
                    q.put("optionC", qRs.getString("optionC"));
                    q.put("optionD", qRs.getString("optionD"));
                    q.put("correct", qRs.getString("correct"));
                    questions.add(q);
                }
                quiz.put("questions", questions);
                quiz.put("totalQuestions", questions.size());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quiz;
    }

    // ================= GET QUESTIONS BY QUIZ ID ✅ =================
    @GetMapping("/questions/{quizId}")
    public List<Map<String, Object>> getQuestionsByQuizId(@PathVariable int quizId) {
        List<Map<String, Object>> questions = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM questions WHERE quiz_id = ?");
            stmt.setInt(1, quizId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> q = new HashMap<>();
                q.put("id", rs.getInt("id"));
                q.put("questionText", rs.getString("question"));
                q.put("optionA", rs.getString("optionA"));
                q.put("optionB", rs.getString("optionB"));
                q.put("optionC", rs.getString("optionC"));
                q.put("optionD", rs.getString("optionD"));
                q.put("correct", rs.getString("correct"));
                questions.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    // ================= CREATE QUIZ =================
    @PostMapping("/quiz")
    public Map<String, Object> createQuiz(@RequestBody Map<String, Object> quizData) {
        Map<String, Object> response = new HashMap<>();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO quizzes(title, description, category, difficulty, duration) VALUES(?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, (String) quizData.get("title"));
            stmt.setString(2, (String) quizData.get("description"));
            stmt.setString(3, (String) quizData.get("category"));
            stmt.setString(4, (String) quizData.get("difficulty"));
            stmt.setInt(5, Integer.parseInt(quizData.get("duration").toString()));
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                response.put("id", keys.getInt(1));
            }
            response.put("status", "Quiz created successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
        }
        return response;
    }

    // ================= UPDATE QUIZ =================
    @PutMapping("/quiz/{id}")
    public Map<String, Object> updateQuiz(@PathVariable int id, @RequestBody Map<String, Object> quizData) {
        Map<String, Object> response = new HashMap<>();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE quizzes SET title=?, description=?, category=?, difficulty=?, duration=? WHERE id=?");
            stmt.setString(1, (String) quizData.get("title"));
            stmt.setString(2, (String) quizData.get("description"));
            stmt.setString(3, (String) quizData.get("category"));
            stmt.setString(4, (String) quizData.get("difficulty"));
            stmt.setInt(5, Integer.parseInt(quizData.get("duration").toString()));
            stmt.setInt(6, id);
            int rows = stmt.executeUpdate();
            response.put("updatedRows", rows);
            response.put("status", "Quiz updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
        }
        return response;
    }

    // ================= DELETE QUIZ =================
    @DeleteMapping("/quiz/{id}")
    public Map<String, Object> deleteQuiz(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try (Connection conn = DBConnection.getConnection()) {
            // ✅ Pehle questions delete karo, phir quiz
            PreparedStatement delQ = conn.prepareStatement("DELETE FROM questions WHERE quiz_id=?");
            delQ.setInt(1, id);
            delQ.executeUpdate();

            PreparedStatement stmt = conn.prepareStatement("DELETE FROM quizzes WHERE id=?");
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            response.put("deletedRows", rows);
            response.put("status", "Quiz deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
        }
        return response;
    }

    // ================= ADD QUESTION ✅ =================
    @PostMapping("/question")
    public Map<String, Object> addQuestion(@RequestBody Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO questions(quiz_id, question, optionA, optionB, optionC, optionD, correct) VALUES(?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, Integer.parseInt(data.get("quizId").toString()));
            stmt.setString(2, (String) data.get("questionText"));
            stmt.setString(3, (String) data.get("optionA"));
            stmt.setString(4, (String) data.get("optionB"));
            stmt.setString(5, (String) data.get("optionC"));
            stmt.setString(6, (String) data.get("optionD"));
            stmt.setString(7, (String) data.get("correctAnswer"));
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                response.put("id", keys.getInt(1));
            }
            response.put("status", "Question added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
        }
        return response;
    }

    @PutMapping("/question/{id}")
    public Map<String, Object> updateQuestion(
            @PathVariable int id,
            @RequestBody Map<String, Object> data) {

        Map<String, Object> response = new HashMap<>();

        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE questions SET question=?, optionA=?, optionB=?, optionC=?, optionD=?, correct=? WHERE id=?");

            stmt.setString(1, (String) data.get("questionText"));
            stmt.setString(2, (String) data.get("optionA"));
            stmt.setString(3, (String) data.get("optionB"));
            stmt.setString(4, (String) data.get("optionC"));
            stmt.setString(5, (String) data.get("optionD"));
            stmt.setString(6, (String) data.get("correctAnswer"));
            stmt.setInt(7, id);

            stmt.executeUpdate();

            response.put("status", "Question Updated");

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
        }

        return response;
    }

    // ================= DELETE QUESTION ✅ =================
    @DeleteMapping("/question/{id}")
    public Map<String, Object> deleteQuestion(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM questions WHERE id=?");
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            response.put("deletedRows", rows);
            response.put("status", "Question deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
        }
        return response;
    }

    // ================= SUBMIT QUIZ =================
    @PostMapping("/submit-quiz")
    public Map<String, Object> submitQuiz(@RequestBody SubmitRequest request) {
        Map<String, Object> result = new HashMap<>();
        int score = 0;
        int totalQuestions = 0;
        try (Connection conn = DBConnection.getConnection()) {
            String username = request.getUsername();
            if (username == null || username.isEmpty()) {
                username = "guest";
            }
            PreparedStatement stmt = conn.prepareStatement("SELECT id, correct FROM questions WHERE quiz_id = ?");
            stmt.setInt(1, request.getQuizId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                totalQuestions++;
                int questionId = rs.getInt("id");
                String correct = rs.getString("correct");
                String userAnswer = request.getAnswers().get(String.valueOf(questionId));
                if (userAnswer != null && userAnswer.equalsIgnoreCase(correct)) {
                    score++;
                }
            }
            PreparedStatement saveStmt = conn
                    .prepareStatement("INSERT INTO results(username, quiz_id, score) VALUES(?,?,?)");
            saveStmt.setString(1, username);
            saveStmt.setInt(2, request.getQuizId());
            saveStmt.setInt(3, score);
            saveStmt.executeUpdate();

            result.put("score", score);
            result.put("totalQuestions", totalQuestions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

// ================= DTO =================
class SubmitRequest {
    private String username;
    private int quizId;
    private Map<String, String> answers;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }
}