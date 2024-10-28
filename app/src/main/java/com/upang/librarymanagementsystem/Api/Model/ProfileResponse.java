package com.upang.librarymanagementsystem.Api.Model;

import android.service.autofill.UserData;

import java.util.List;

public class ProfileResponse {
    private boolean success;
    private User data; // Change this to User instead of List<User>

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public User getData() { // Change return type to User
        return data;
    }

    public void setData(User data) { // Change parameter type to User
        this.data = data;
    }

}
