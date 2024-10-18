package com.upang.librarymanagementsystem.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.upang.librarymanagementsystem.Api.Interfaces.BooksApiService;
import com.upang.librarymanagementsystem.Api.Client.RetrofitClient;
import com.upang.librarymanagementsystem.Api.Interfaces.UserClient;
import com.upang.librarymanagementsystem.Api.Model.Books; // Import your Books model class
import com.upang.librarymanagementsystem.Api.Model.BooksResponse;
import com.upang.librarymanagementsystem.Api.Model.ProfileResponse;
import com.upang.librarymanagementsystem.Api.Model.User;
import com.upang.librarymanagementsystem.R;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebPage extends AppCompatActivity {
    private Button btnLogout; // Declare the logout button
    private UserClient userClient; // Declare the UserClient
    private BooksApiService booksApiService; // Declare the BooksApiService

    // Declare TextViews for book details
    private TextView authorTextView;
    private TextView bookTitleTextView;
    private TextView bookCopiesTextView;
    private TextView publisherTextView;
    private TextView descriptionTextView;
    private TextView bookCoverTextView;
    private TextView locationTextView;
    private TextView statusTextView;
    private TextView categoryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_webpage);

        // Initialize the UserClient and BooksApiService instances
        userClient = RetrofitClient.getInstance(getApplicationContext()).getApi();
        booksApiService = RetrofitClient.getInstance(getApplicationContext()).getBooksApiService();

        // Initialize TextView references
        authorTextView = findViewById(R.id.Author);
        bookTitleTextView = findViewById(R.id.Booktitle);
        bookCopiesTextView = findViewById(R.id.Bookcopies);
        publisherTextView = findViewById(R.id.Publisher);
        descriptionTextView = findViewById(R.id.Description);
        bookCoverTextView = findViewById(R.id.Bookcover);
        locationTextView = findViewById(R.id.Location);
        statusTextView = findViewById(R.id.Status);
        categoryTextView = findViewById(R.id.Category);

        btnLogout = findViewById(R.id.btnLogout); // Initialize the logout button

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
                Log.d("Button Test", "buttonClicked");

            }
        });
        fetchProfile();
    }


    private void fetchBooks() {
        // Retrieve the token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (token != null) {
            // Make the API call
            Call<BooksResponse> call = booksApiService.getBooks("Bearer " + token);
            call.enqueue(new Callback<BooksResponse>() {
                @Override
                public void onResponse(Call<BooksResponse> call, Response<BooksResponse> response) {
                    // Log the response body
                        Log.d("FetchBooksResponse", "Response: " + response.raw().toString());

                        if (response.isSuccessful()) {
                            BooksResponse booksResponse = response.body(); // Get the response body
                            if (booksResponse != null && booksResponse.getData() != null) {
                                List<Books> booksList = booksResponse.getData(); // Get the list of books
                                if (!booksList.isEmpty()) {
                                    // Display the first book in the list
                                    Books book = booksList.get(0); // or iterate through the list if needed

                                    // Set the data in the TextViews
                                    authorTextView.setText(book.getAuthor());
                                    bookTitleTextView.setText(book.getBooktitle());
                                    bookCopiesTextView.setText(String.valueOf(book.getBookcopies()));
                                    publisherTextView.setText(book.getPublisher());
                                    descriptionTextView.setText(book.getDescription());
                                    bookCoverTextView.setText(book.getBookcover());
                                    locationTextView.setText(book.getLocation());
                                    statusTextView.setText(book.getStatus() == 1 ? "Available" : "Not Available");
                                    categoryTextView.setText(book.getCategory());
                                } else {
                                    Toast.makeText(WebPage.this, "No books found", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(WebPage.this, "Failed to fetch books: No data available", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(WebPage.this, "Failed to fetch books: " + response.message(), Toast.LENGTH_LONG).show();
                        }
                }

                @Override
                public void onFailure(Call<BooksResponse> call, Throwable t) {
                    Toast.makeText(WebPage.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "Token is null. Please log in again.", Toast.LENGTH_LONG).show();
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

    private void fetchProfile(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (token != null){
            Call<ProfileResponse> call = RetrofitClient
                    .getInstance(getApplicationContext())
                    .getApi()
                    .getProfile("Bearer " + token);

            call.enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                    Log.d("FetchBooksResponse", "Response: " + response.raw().toString());

                    if (response.isSuccessful()) {
                        ProfileResponse profileResponse = response.body(); // Get the response body
                        if (profileResponse != null && profileResponse.getData() != null) {
                            List<User> profile = profileResponse.getData(); // Get the list of books
                            if (!profile.isEmpty()) {
                                // Display the first book in the list
                                User user = profile.get(0); // or iterate through the list if needed

                                // Set the data in the TextViews
                                authorTextView.setText(user.getFirstname());
                                bookTitleTextView.setText(user.getLastname());
//                                bookCopiesTextView.setText(String.valueOf(book.getBookcopies()));
//                                publisherTextView.setText(book.getPublisher());
//                                descriptionTextView.setText(book.getDescription());
//                                bookCoverTextView.setText(book.getBookcover());
//                                locationTextView.setText(book.getLocation());
//                                statusTextView.setText(book.getStatus() == 1 ? "Available" : "Not Available");
//                                categoryTextView.setText(book.getCategory());
                            } else {
                                Toast.makeText(WebPage.this, "No books found", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(WebPage.this, "Failed to fetch books: No data available", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(WebPage.this, "Failed to fetch books: " + response.message(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                    Toast.makeText(WebPage.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
