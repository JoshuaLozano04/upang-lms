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
import com.upang.librarymanagementsystem.Api.Interfaces.UserClient;
import com.upang.librarymanagementsystem.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Settings extends AppCompatActivity {
    ImageButton btnBackSetting;
    TextView btnEditProfile;
    TextView btnChangePass;
    TextView btnTerms;
    TextView btnPrivacy;
    TextView btnAboutLMS;
    TextView btnLogOut;
    private UserClient userClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBackSetting = findViewById(R.id.btnBackSetting);

        btnBackSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, AccountPage.class);
                startActivity(intent);
                finish();
            }
        });

        btnEditProfile = findViewById(R.id.btnEditProfile);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, EditProfile.class);
                startActivity(intent);
                finish();
            }
        });

        btnChangePass = findViewById(R.id.btnChangePass);

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, ChangePassword.class);
                startActivity(intent);
                finish();
            }
        });

        btnTerms = findViewById(R.id.btnTerms);

        btnTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, TermsAndCondition.class);
                startActivity(intent);
                finish();
            }
        });

        btnPrivacy = findViewById(R.id.btnPrivacy);

        btnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, PrivacyPolicy.class);
                startActivity(intent);
                finish();
            }
        });

        btnAboutLMS = findViewById(R.id.btnAboutLMS);

        btnAboutLMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, AboutLMS.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogOut = findViewById(R.id.btnLogOut);

        userClient = RetrofitClient.getInstance(getApplicationContext()).getApi();

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }
    private void logOut(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (token != null){
            Call<ResponseBody> call = userClient.logout("Bearer " + token);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("auth_token"); // Remove the token
                        editor.apply();
                        Toast.makeText(Settings.this, "Logout Successful", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Settings.this, LoginPage.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Settings.this, "Logout Fail", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                }
            });
        }
    }
}