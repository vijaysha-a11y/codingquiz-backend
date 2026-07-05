package com.quizapp;

public record Leaderboard() {

	public static void showLeaderboard() {
		try {
			com.quiz.Leaderboard.showLeaderboard();
		} catch (Exception ex) {
			System.out.println("Unable to load leaderboard: " + ex.getMessage());
		}
	}

}
