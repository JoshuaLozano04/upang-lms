package com.upang.librarymanagementsystem.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.upang.librarymanagementsystem.Api.Client.RetrofitClient;
import com.upang.librarymanagementsystem.Api.Model.ProfileResponse;
import com.upang.librarymanagementsystem.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountPage extends AppCompatActivity {
    ImageButton btnQR;
    ImageButton btnCog;

    TextView nameTextView,emailTextView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        btnQR = findViewById(R.id.btnQR);
        nameTextView = findViewById(R.id.tvName);
        emailTextView = findViewById(R.id.tvEmail);

        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        fetchProfileAndDisplay(token,nameTextView);

        btnQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountPage.this, QRCode.class);
                startActivity(intent);
                finish();
            }
        });

        btnCog = findViewById(R.id.btnCog);

        btnCog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountPage.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });


    }


    public void fetchProfileAndDisplay(String token, TextView infoTextView) {
        Call<ProfileResponse> call = RetrofitClient
                .getInstance()
                .getApi()
                .getProfile(token);

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Fetch the first name, last name, and email from the response
                    String firstName = response.body().getData().getFirstName();
                    String lastName = response.body().getData().getLastName();
                    String email = response.body().getData().getEmail();

                    // Combine the name and email, and set the text on the TextView
                    String combinedInfo = firstName + " " + lastName;
                    nameTextView.setText(combinedInfo);
                    emailTextView.setText(email);
                } else {
                    Toast.makeText(AccountPage.this, "Error fetching user data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable throwable) {
                Toast.makeText(AccountPage.this, "Error fetching user data", Toast.LENGTH_LONG).show();

            }
        });
    }

}
