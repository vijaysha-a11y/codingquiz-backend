package com.quizapp;

import java.util.InputMismatchException;
import java.util.Scanner;
import com.modules.User;

public class Main {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
                System.out.println("Welcome to Online Quiz App!");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose option: ");

                int choice;
                try {
                    choice = sc.nextInt();
                    sc.nextLine();
                } catch (InputMismatchException ex) {
                    sc.nextLine();
                    System.out.println("Invalid choice!");
                    continue;
                }

                switch (choice) {
                    case 1:
                        System.out.print("Enter username: ");
                        String regUser = sc.nextLine();
                        System.out.print("Enter password: ");
                        String regPass = sc.nextLine();
                        if (User.registerUser(regUser, regPass)) {
                            System.out.println("Registration successful!");
                        } else {
                            System.out.println("Registration failed!");
                        }
                        break;

                    case 2:
                        System.out.print("Enter username: ");
                        String loginUser = sc.nextLine();
                        System.out.print("Enter password: ");
                        String loginPass = sc.nextLine();
                        // ✅ FIX: String return hoti hai ab, boolean nahi
                        String role = User.loginUser(loginUser, loginPass);
                        if (role != null) {
                            System.out.println("Login successful! Role: " + role);
                        } else {
                            System.out.println("Invalid credentials!");
                        }
                        break;

                    case 3:
                        System.out.println("Exiting...");
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }
            }
        }
    }
}