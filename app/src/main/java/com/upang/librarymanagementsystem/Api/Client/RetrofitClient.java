package com.upang.librarymanagementsystem.Api.Client;

import com.upang.librarymanagementsystem.Api.Interfaces.UserClient;

import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://top-stable-octopus.ngrok-free.app/api/";
    private static RetrofitClient mInstance;
    private final Retrofit retrofit;
    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static synchronized RetrofitClient getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }
    public UserClient getApi(){
        return retrofit.create(UserClient.class);
    }
}
