package com.quizapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.quizapp", "com.controller", "com.modules"})
public class OnlineQuizApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineQuizApplication.class, args);
    }
}