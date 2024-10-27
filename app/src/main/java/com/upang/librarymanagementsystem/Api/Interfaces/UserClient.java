package com.upang.librarymanagementsystem.Api.Interfaces;
import com.upang.librarymanagementsystem.Api.Model.BookDetailResponse;
import com.upang.librarymanagementsystem.Api.Model.BookListResponse;
import com.upang.librarymanagementsystem.Api.Model.Login;
import com.upang.librarymanagementsystem.Api.Model.ProfileResponse;
import com.upang.librarymanagementsystem.Api.Model.UpdatePasswordRequest;
import com.upang.librarymanagementsystem.Api.Model.User;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserClient {
    @POST("login/")  // Correct endpoint for login
    Call<User> login(@Body Login login);

    @POST("logout") // Adjust the endpoint as necessary
    Call<ResponseBody> logout(@Header("Authorization") String token);

    @GET("profile")
    Call<ProfileResponse> getProfile(@Header("Authorization") String token);


    @FormUrlEncoded
    @POST("register")  // Ensure the endpoint is correct
    Call<ResponseBody> register(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("password") String password
    );

    // Fetch all books
    @GET("Books")
    Call<BookListResponse> getBooks(@Header("Authorization") String token);

    @GET("Books/{id}")
    Call<BookDetailResponse> getBookDetails(@Path("id") int id, @Header("Authorization") String token);


    @GET("storage/{imagePath}")
    Call<ResponseBody> getBookCover(@Path("imagePath") String imagePath, @Header("Authorization") String token);

    @FormUrlEncoded
    @POST("update-profile")
    Call<ResponseBody> updateProfile(
            @Header("Authorization") String token,
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("password") String password,
            @Field("password_confirmation") String confirmPassword
            );

    @FormUrlEncoded
    @POST("forgot-password")
    Call<ResponseBody> forgetPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("reset-password")
    Call<ResponseBody> resetPassword(
            @Field("email") String email,
            @Field("token") String token,
            @Field("password") String password,
            @Field("password_confirmation") String confirmPassword
    );
}
