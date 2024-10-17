package com.upang.librarymanagementsystem.Api.Interfaces;

import com.upang.librarymanagementsystem.Api.Model.BooksResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface BooksApiService {
    @GET("Books/") // Ensure this matches the endpoint you're calling
    Call<BooksResponse> getBooks(@Header("Authorization") String token);
}
