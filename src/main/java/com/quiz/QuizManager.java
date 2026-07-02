package com.quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuizManager {
    public static void addQuiz(String title) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO quizzes(title) VALUES(?)");
            stmt.setString(1, title);
            stmt.executeUpdate();
        }
    }

    public static void addQuestion(int quizId, String question, String optionA, String optionB, String optionC,
            String optionD, String correctAnswer) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO questions(quiz_id, question, optionA, optionB, optionC, optionD, correct) VALUES(?,?,?,?,?,?,?)");
            stmt.setInt(1, quizId);
            stmt.setString(2, question);
            stmt.setString(3, optionA);
            stmt.setString(4, optionB);
            stmt.setString(5, optionC);
            stmt.setString(6, optionD);
            stmt.setString(7, correctAnswer);
            stmt.executeUpdate();
        }
    }
}
