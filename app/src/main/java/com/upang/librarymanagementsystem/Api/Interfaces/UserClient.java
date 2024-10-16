package com.upang.librarymanagementsystem.Api.Interfaces;

import com.upang.librarymanagementsystem.Api.Model.ChangePasswordRequest;
import com.upang.librarymanagementsystem.Api.Model.Login;
import com.upang.librarymanagementsystem.Api.Model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserClient {
    @POST("api/login")
    Call<User> login(@Body Login login);

    @FormUrlEncoded
    @POST("api/register")
    Call<ResponseBody> register(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("auth/store")
    Call<ResponseBody> updatePassword(
            @Header("Authorization") String token,
            @Body ChangePasswordRequest changePasswordRequest
            );


    @POST("api/Books")
    Call<ResponseBody> getSecret(@Header("Authorization") String authToken);
}
