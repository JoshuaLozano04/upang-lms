package com.upang.librarymanagementsystem.Api.Model;

public class ChangePasswordRequest {
    private String current_password;
    private String password;

    public ChangePasswordRequest(String currentPassword, String newPassword) {
        this.current_password = currentPassword;
        this.password = newPassword;
    }
}
