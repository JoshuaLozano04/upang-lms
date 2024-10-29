package com.upang.librarymanagementsystem.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.upang.librarymanagementsystem.Api.Client.RetrofitClient;
import com.upang.librarymanagementsystem.Api.Interfaces.UserClient;
import com.upang.librarymanagementsystem.Api.Model.BookDetailResponse;
import com.upang.librarymanagementsystem.Api.Model.BookList;
import com.upang.librarymanagementsystem.R;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetails extends AppCompatActivity {
    private TextView tvTitle, tvAuthor, tvDescription, tvSubject, tvLocation, tvCopies, tvPublisher, tvStatus, tvShowMore; // Add tvShowMore here
    private ImageView bookCover; // For displaying the book cover
    private UserClient userClient;
    private ImageButton btnBackWebpage;

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
        tvShowMore = findViewById(R.id.tvShowMore); // Initialize tvShowMore
        tvSubject = findViewById(R.id.tvSubject);
        tvLocation = findViewById(R.id.tvLocation);
        tvCopies = findViewById(R.id.tvCopies);
        tvPublisher = findViewById(R.id.tvPublisher);
        tvStatus = findViewById(R.id.tvStatus);
        bookCover = findViewById(R.id.ivBookCover);
        btnBackWebpage = findViewById(R.id.btnBackWebpage);




        btnBackWebpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookDetails.this, WebPage.class);
                startActivity(intent);
                finish();
                SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("selected_book_id");
                editor.apply();
            }
        });

        userClient = RetrofitClient.getInstance(getApplicationContext()).getApi();

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        bookId = sharedPreferences.getInt("selected_book_id", -1);

        if (bookId != -1) {
            fetchBookDetails(bookId);
        } else {
            Toast.makeText(this, "No book selected", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if no book is selected
        }

        // Set the initial visibility and click listener for expand/collapse
        tvShowMore.setOnClickListener(new View.OnClickListener() {
            private boolean isExpanded = false; // Track expansion state

            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    tvDescription.setMaxLines(3); // Collapse to 3 lines
                    tvShowMore.setText("Show More");
                } else {
                    tvDescription.setMaxLines(Integer.MAX_VALUE); // Expand fully
                    tvShowMore.setText("Show Less");
                }
                isExpanded = !isExpanded; // Toggle the state
            }
        });
    }

    private void fetchBookDetails(int bookId) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (token != null) {
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

    private OkHttpClient client = new OkHttpClient();

    private void fetchImage(String imageUrl) {
        Request request = new Request.Builder()
                .url(imageUrl)
                .addHeader("Authorization", "Bearer " + getToken())
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    byte[] imageData = response.body().bytes();

                    runOnUiThread(() -> {
                        Glide.with(BookDetails.this)
                                .load(imageData)
                                .transform(new RoundedCorners(20))
                                .into(bookCover);
                    });
                } else {
                    Log.d("FetchImage", "Error fetching image: " + response.message());
                }
            }
        });
    }


    // Method to get the token
    private String getToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", null);
    }

    private void displayBookDetails(BookList book) {
        tvTitle.setText(book.getBookTitle());
        tvAuthor.setText(book.getAuthor());
        tvDescription.setText(book.getDescription());
        tvPublisher.setText(book.getPublisher());
        tvCopies.setText(book.getBookcopies());
        tvSubject.setText(book.getCategory());
        tvLocation.setText(book.getLocation());
        if (book.getStatus() != null) {
            int statusValue = Integer.parseInt(book.getStatus());
            if (statusValue == 1) {
                tvStatus.setText("Available");
            } else {
                tvStatus.setText("Not Available");
            }
        } else {
            tvStatus.setText("Status not available");
        }
        String bookCoverPath = "http://192.168.18.138:8000/storage/" + book.getBookCover();
        fetchImage(bookCoverPath);
    }
}
