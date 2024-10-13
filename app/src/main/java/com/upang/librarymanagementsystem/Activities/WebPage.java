package com.upang.librarymanagementsystem.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.upang.librarymanagementsystem.Api.Interfaces.UserClient;
import com.upang.librarymanagementsystem.Api.Client.RetrofitClient;
import com.upang.librarymanagementsystem.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebPage extends AppCompatActivity {
    private Button btnLogout; // Declare the logout button
    private UserClient userClient; // Declare the UserClient

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_webpage);

        // Initialize the UserClient instance
        userClient = RetrofitClient.getInstance().getApi(); // Change this line

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLogout = findViewById(R.id.btnLogout); // Initialize the logout button

        // Handle logout button click
        btnLogout.setOnClickListener(view -> logout());
    }

    private void logout() {
        // Retrieve the token from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (token != null) {
            // Call the logout API
            Call<ResponseBody> call = userClient.logout("Bearer " + token);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // Clear the token from SharedPreferences
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
                        Toast.makeText(WebPage.this, "Logout failed: " + response.message(), Toast.LENGTH_LONG).show();
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
