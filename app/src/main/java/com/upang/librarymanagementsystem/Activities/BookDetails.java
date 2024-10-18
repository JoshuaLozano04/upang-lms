package com.upang.librarymanagementsystem.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.upang.librarymanagementsystem.Api.Client.RetrofitClient;
import com.upang.librarymanagementsystem.Api.Interfaces.UserClient;
import com.upang.librarymanagementsystem.Api.Model.BookDetailResponse;
import com.upang.librarymanagementsystem.Api.Model.BookList;
import com.upang.librarymanagementsystem.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetails extends AppCompatActivity {
    private TextView tvTitle, tvAuthor, tvDescription, tvSubject, tvLocation, tvCopies, tvPublisher, tvStatus; // Add other TextViews as needed
    private ImageView bookCover; // For displaying the book cover
    private UserClient userClient;
    private int bookId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;



        });
        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvDescription = findViewById(R.id.tvDescription);
        tvSubject = findViewById(R.id.tvSubject);
        tvLocation = findViewById(R.id.tvLocation);
        tvCopies = findViewById(R.id.tvCopies);
        tvPublisher = findViewById(R.id.tvPublisher);
        tvStatus = findViewById(R.id.tvStatus);
        userClient = RetrofitClient.getInstance(getApplicationContext()).getApi();

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        bookId = sharedPreferences.getInt("selected_book_id", -1);

        if (bookId != -1) {
            fetchBookDetails(bookId);
        } else {
            Toast.makeText(this, "No book selected", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if no book is selected
        }
    }

    private void fetchBookDetails(int bookId) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (token != null){
            Call<BookDetailResponse> call = userClient.getBookDetails(bookId, "Bearer " + token);
            call.enqueue(new Callback<BookDetailResponse>() {
                @Override
                public void onResponse(Call<BookDetailResponse> call, Response<BookDetailResponse> response) {
                    if (response.isSuccessful()) {
                        BookList book = response.body().getData(); // Get the single BookList object
                        displayBookDetails(book);
                    } else {
                        Log.d("FetchBookDetails", "Failed to fetch book details: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<BookDetailResponse> call, Throwable throwable) {
                    Log.d("FetchBookDetails", "Error: " + throwable.getMessage());
                }
            });

        }

    }
    private void displayBookDetails(BookList book) {
        tvTitle.setText(book.getBookTitle());
        tvAuthor.setText(book.getAuthor());
        tvDescription.setText(book.getDescription());
        tvPublisher.setText(book.getPublisher());
        tvStatus.setText(book.getStatus());
        tvCopies.setText(book.getBookcopies());
        tvSubject.setText(book.getCategory());
        tvLocation.setText(book.getLocation());
    }
}