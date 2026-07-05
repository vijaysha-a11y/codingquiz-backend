package com.quizapp;

public record QuizTaker() {

	public static void takeQuiz(int i, String user) {
		try {
			com.quiz.QuizTaker.takeQuiz(i, user);
		} catch (Exception ex) {
			System.out.println("Unable to start quiz: " + ex.getMessage());
		}
		
	}

}
