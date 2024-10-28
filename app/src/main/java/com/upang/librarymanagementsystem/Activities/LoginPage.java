package com.upang.librarymanagementsystem.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.upang.librarymanagementsystem.Api.Interfaces.UserClient;
import com.upang.librarymanagementsystem.Api.Model.Login;
import com.upang.librarymanagementsystem.Api.Model.User;
import com.upang.librarymanagementsystem.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginPage extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    TextView btnforgot;
    TextView btnCreateNA;

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl("https://decent-hardy-mastodon.ngrok-free.app/api/")
            .addConverterFactory(GsonConverterFactory.create());

    Retrofit retrofit = builder.build();
    UserClient userClient = retrofit.create(UserClient.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etEmail = findViewById(R.id.EtEmail);
        etPassword = findViewById(R.id.EtPass);
        btnLogin = findViewById(R.id.btnLogin);

        // Check if a token is already stored
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        // If a token exists, go directly to WebPage
        if (token != null) {
            Intent intent = new Intent(LoginPage.this, WebPage.class);
            startActivity(intent);
            finish();
            return; // Exit the onCreate method early to avoid executing the rest of the code
        }

        // Handle login button click
        btnLogin.setOnClickListener(view -> {
            Log.d("LoginButton", "Login button clicked");
            login();
        });

        btnCreateNA = findViewById(R.id.btnCreateNA);

        btnCreateNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        btnforgot = findViewById(R.id.btnforgot);

        btnforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Login login = new Login(email, password);
        Call<User> call = userClient.login(login);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("auth_token", token);
                    editor.apply();
                    Intent intent = new Intent(LoginPage.this, WebPage.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginPage.this, "Error: Could not log in:" + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                Toast.makeText(LoginPage.this, "Error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
