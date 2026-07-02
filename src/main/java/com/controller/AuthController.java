package com.controller;

import org.springframework.web.bind.annotation.*;
import com.modules.User;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody UserRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Username is required");
                return response;
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Password is required");
                return response;
            }

            boolean registered = User.registerUser(request.getUsername(), request.getPassword());
            response.put("success", registered);
            response.put("message", registered ? "User registered successfully" : "Username already exists!");
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return response;
        }
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody UserRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Username is required");
                return response;
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "Password is required");
                return response;
            }

            // ✅ role return hogi — null matlab login fail
            String role = User.loginUser(request.getUsername(), request.getPassword());

            if (role != null) {
                response.put("success", true);
                response.put("username", request.getUsername());
                response.put("role", role); // ✅ ADMIN ya USER
                response.put("message", "Login successful");
            } else {
                response.put("success", false);
                response.put("message", "Invalid username or password");
            }

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error: " + e.getMessage());
            return response;
        }
    }
}

class UserRequest {
    private String username;
    private String password;
    private String email;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}