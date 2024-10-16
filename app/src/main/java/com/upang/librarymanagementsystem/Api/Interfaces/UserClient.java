package com.upang.librarymanagementsystem.Api.Interfaces;

import com.upang.librarymanagementsystem.Api.Model.ChangePasswordRequest;
import com.upang.librarymanagementsystem.Api.Model.Login;
import com.upang.librarymanagementsystem.Api.Model.ProfileResponse;
import com.upang.librarymanagementsystem.Api.Model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {
    @POST("login")
    Call<User> login(@Body Login login);

    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> register(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("password") String password
    );

    @POST("store")
    Call<ResponseBody> updatePassword(
            @Header("Authorization") String token,
            @Body ChangePasswordRequest changePasswordRequest
            );

    @GET("profile")
    Call<ProfileResponse> getProfile(@Header("Authorization") String token);

    @POST("logout")
    Call<Void> logout(@Header("Authorization") String token);


    @POST("api/Books")
    Call<ResponseBody> getSecret(@Header("Authorization") String authToken);
}
