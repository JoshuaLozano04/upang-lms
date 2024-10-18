package com.upang.librarymanagementsystem.Api.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BookListResponse {
    @SerializedName("data")
    private ArrayList<BookList> data;

    public ArrayList<BookList> getData() {
        return data;
    }

    public void setData(ArrayList<BookList> data) {
        this.data = data;
    }
}
