package com.upang.librarymanagementsystem.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.upang.librarymanagementsystem.Api.Adapter.RvBooksAdapter;
import com.upang.librarymanagementsystem.Api.Client.RetrofitClient;
import com.upang.librarymanagementsystem.Api.Interfaces.UserClient;
import com.upang.librarymanagementsystem.Api.Model.BookList;
import com.upang.librarymanagementsystem.Api.Model.BookListResponse;
import com.upang.librarymanagementsystem.R;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebPage extends AppCompatActivity {
//    private Button btnLogout; // Declare the logout button
    private UserClient userClient; // Declare the UserClient

    RecyclerView rv_bookdisplay;
    RvBooksAdapter rvBooksAdapter;
    ArrayList<BookList> bookLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_webpage);
        userClient = RetrofitClient.getInstance(getApplicationContext()).getApi();
//      btnLogout = findViewById(R.id.btnLogout);

        bookLists = new ArrayList<>();
        rv_bookdisplay = findViewById(R.id.rv_bookdisplay);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(this,3);
        rv_bookdisplay.setLayoutManager(gridLayoutManager);
        rvBooksAdapter = new RvBooksAdapter(WebPage.this,bookLists);
        rv_bookdisplay.setAdapter(rvBooksAdapter);
        fetchBooks();
//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                logOut();
//                Log.d("Button Test", "buttonClicked");
//
//            }
//        });



    }
    private void fetchBooks(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (token != null){
            Call<BookListResponse> call = userClient.getBooks("Bearer " + token);
            call.enqueue(new Callback<BookListResponse>() {
                @Override
                public void onResponse(Call<BookListResponse> call, Response<BookListResponse> response) {
                    Log.d("API Response Code", String.valueOf(response.code())); // Log the response code
                    if (response.code() == 200){
                        if(response.isSuccessful()){
                            bookLists.clear();
                            bookLists.addAll(response.body().getData());
                            rvBooksAdapter.notifyDataSetChanged();
                            Log.d("FetchBooks", "Books fetched successfully: " + bookLists.size());
                        } else {
                        Log.d("FetchBooks", "Failed to fetch books: " + response.message());
                        }
                    }else {
                        Log.d("FetchBooks", "Failed to fetch books: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<BookListResponse> call, Throwable throwable) {

                }
            });
        }
    }



    private void logOut() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        if (token != null) {
            // Call the logout API
            Call<ResponseBody> call = userClient.logout("Bearer " + token);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // Handle successful logout
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("auth_token");
                        editor.apply();

                        // Redirect to LoginPage
                        Intent intent = new Intent(WebPage.this, LoginPage.class);
                        startActivity(intent);
                        finish();

                        // Optional: Show a toast for confirmation
                        Toast.makeText(WebPage.this, "Logged out successfully", Toast.LENGTH_LONG).show();
                    } else {
                        // Handle unsuccessful logout
                        if (response.code() == 401) {
                            // Session expired, inform the user
                            Toast.makeText(WebPage.this, "Session expired. Please log in again.", Toast.LENGTH_LONG).show();
                            // Optionally, redirect to login
                            Intent intent = new Intent(WebPage.this, LoginPage.class);
                            startActivity(intent);
                            finish(); // Call finish to close the current activity
                        } else {
                            Toast.makeText(WebPage.this, "Logout failed: " + response.message(), Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Handle error
                    Toast.makeText(WebPage.this, "Logout Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // Token is null, handle error
            Toast.makeText(WebPage.this, "You are not logged in", Toast.LENGTH_LONG).show();
        }
    }
}
