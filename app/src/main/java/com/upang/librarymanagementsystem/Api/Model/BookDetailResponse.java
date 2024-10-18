package com.upang.librarymanagementsystem.Api.Model;

import com.google.gson.annotations.SerializedName;

public class BookDetailResponse {
    @SerializedName("data")
    private BookList data; // Change this to hold a single book detail instead of a list
    public BookList getData() {
        return data;
    }

    public void setData(BookList data) {
        this.data = data;
    }
}
