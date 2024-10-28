package com.upang.librarymanagementsystem.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.upang.librarymanagementsystem.Api.Client.RetrofitClient;
import com.upang.librarymanagementsystem.Api.Interfaces.UserClient;
import com.upang.librarymanagementsystem.Api.Model.ProfileResponse;
import com.upang.librarymanagementsystem.Api.Model.User;
import com.upang.librarymanagementsystem.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountPage extends AppCompatActivity {
    ImageButton btnBackAcc;
    ImageButton btnQR;
    ImageButton btnCog;
    TextView Name, Email, Id;
    private UserClient userClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userClient = RetrofitClient.getInstance(getApplicationContext()).getApi();

        Name = findViewById(R.id.tvName);
        Email = findViewById(R.id.tvEmail);

        btnBackAcc = findViewById(R.id.btnBackAcc);

        btnBackAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountPage.this, WebPage.class);
                startActivity(intent);
                finish();
            }
        });

        btnQR = findViewById(R.id.btnQR);

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

        fetchProfile();
    }
    private void fetchProfile(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (token != null) {
            Call<ProfileResponse> call = userClient.getProfile("Bearer " + token);
            call.enqueue(new Callback<ProfileResponse>() {
                @Override
                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ProfileResponse profileResponse = response.body();
                        User user = profileResponse.getData();


                        if (user != null) {
                            String fullName = capitalizeFirstLetter(user.getFirstname()) + " " + capitalizeFirstLetter(user.getLastname());
                            Log.d("Profile", "User Profile: " + fullName);

                            Name.setText(fullName);
                            Email.setText(user.getEmail());




                        } else {
                            Log.d("Profile", "No user data found");
                        }
                    } else {
                        Log.d("Profile", "Failed to fetch user profile: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable throwable) {
                    Log.d("Profile", "Error: " + throwable.getMessage());
                }
            });
        } else {
            Log.d("Profile", "Token is null, cannot fetch profile");
        }

    }
    private static String capitalizeFirstLetter(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
