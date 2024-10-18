package com.upang.librarymanagementsystem.Api.Model;

import android.service.autofill.UserData;

import java.util.List;

public class ProfileResponse {
    private boolean success;
    private List<User> data;

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean isSuccess() {
        return success;
    }

    public List<User> getData() {
        return data;
    }
    public void setData(List<User> data) {
        this.data = data;
    }
}
