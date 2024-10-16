package com.upang.librarymanagementsystem.Api.Model;

public class ProfileResponse {
    private String message;
    private User data;

    // Getter methods
    public String getMessage() {
        return message;
    }

    public User getData() {
        return data;
    }

    public static class User {
        private String firstName;
        private String lastName;
        private String email;

        // Getter methods for first name, last name, and email
        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }
    }
}