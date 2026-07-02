package com.modules;

import java.util.List;

public class Quiz {
    private int id;
    private String title;
    private List<Question> questions;

    // Constructors
    public Quiz() {}
    public Quiz(int id, String title) {
        this.id = id;
        this.title = title;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    @Override
    public String toString() {
        return "Quiz{id=" + id + ", title='" + title + "'}";
    }
}
