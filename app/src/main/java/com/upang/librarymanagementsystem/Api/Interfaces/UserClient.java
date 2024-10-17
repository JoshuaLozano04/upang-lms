package com.upang.librarymanagementsystem.Api.Interfaces;
import com.upang.librarymanagementsystem.Api.Model.Books;
import com.upang.librarymanagementsystem.Api.Model.Login;
import com.upang.librarymanagementsystem.Api.Model.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {
    @POST("login/")  // Correct endpoint for login
    Call<User> login(@Body Login login);

    @POST("logout") // Adjust the endpoint as necessary
    Call<ResponseBody> logout(@Header("Authorization") String token);



    @FormUrlEncoded
    @POST("register")  // Ensure the endpoint is correct
    Call<ResponseBody> register(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("email") String email,
            @Field("password") String password
    );


    public interface BooksApiService {

        // Fetch all books
        @GET("Books")
        Call<List<Books>> getBooks();
    }

}
