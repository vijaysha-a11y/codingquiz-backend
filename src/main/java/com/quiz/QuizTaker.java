package com.quiz;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class QuizTaker {
    public static void takeQuiz(int quizId, String username) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM questions WHERE quiz_id=?");
            stmt.setInt(1, quizId);
            ResultSet rs = stmt.executeQuery();

            Scanner sc = new Scanner(System.in);
            int score = 0;

            while (rs.next()) {
                System.out.println(rs.getString("question"));
                System.out.println("A: " + rs.getString("optionA"));
                System.out.println("B: " + rs.getString("optionB"));
                System.out.println("C: " + rs.getString("optionC"));
                System.out.println("D: " + rs.getString("optionD"));

                System.out.print("Your answer: ");
                String ans = sc.nextLine();

                if (ans.equalsIgnoreCase(rs.getString("correct"))) {
                    System.out.println("Correct!");
                    score++;
                } else {
                    System.out.println("Incorrect!");
                }
            }

            System.out.println("Final Score: " + score);
            saveResult(username, quizId, score);
        }
    }

    private static void saveResult(String username, int quizId, int score) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO results(username, quiz_id, score) VALUES(?,?,?)");
            stmt.setString(1, username);
            stmt.setInt(2, quizId);
            stmt.setInt(3, score);
            stmt.executeUpdate();
        }
    }
}
