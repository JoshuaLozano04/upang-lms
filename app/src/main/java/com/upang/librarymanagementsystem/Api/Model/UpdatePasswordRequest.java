package com.upang.librarymanagementsystem.Api.Model;

public class UpdatePasswordRequest {
    private String current_password;
    private String password;
    private String password_confirmation; // This is for confirmed password

    public UpdatePasswordRequest(String currentPassword, String password, String passwordConfirmation) {
        this.current_password = currentPassword;
        this.password = password;
        this.password_confirmation = passwordConfirmation;
    }
}
