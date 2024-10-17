package com.upang.librarymanagementsystem.Api.Client;

import android.content.Context;

import com.upang.librarymanagementsystem.Api.Interfaces.UserClient;
import com.upang.librarymanagementsystem.Api.Interfaces.BooksApiService; // Import the BooksApiService
import com.upang.librarymanagementsystem.Api.Model.BooksResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

public class RetrofitClient {
    private static final String BASE_URL = "https://strangely-pumped-horse.ngrok-free.app/api/";
    private static RetrofitClient mInstance;
    private final Retrofit retrofit;

    private RetrofitClient(Context context) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RetrofitClient(context);
        }
        return mInstance;
    }

    public UserClient getApi() {
        return retrofit.create(UserClient.class);
    }

    public BooksApiService getBooksApiService() {
        return retrofit.create(BooksApiService.class); // Create BooksApiService instance
    }
}


