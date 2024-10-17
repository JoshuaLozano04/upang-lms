package com.upang.librarymanagementsystem.Api.Model;

import java.util.List;

public class BooksResponse {
    private boolean success; // Verify if your API response includes this
    private List<Books> data; // List to hold the books

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Books> getData() {
        return data;
    }

    public void setData(List<Books> data) {
        this.data = data;
    }
}
