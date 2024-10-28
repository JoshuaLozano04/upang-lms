package com.upang.librarymanagementsystem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ForgotPassword extends AppCompatActivity {
    private UserClient userClient;
    private EditText editText7;
    protected void onCreate(Bundle savedInstanceState) {
        ImageView btnForgotBack;
        Button button5;

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnForgotBack = findViewById(R.id.btnForgotBack);

        btnForgotBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassword.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });

        userClient = RetrofitClient.getInstance(getApplicationContext()).getApi();
        button5 = findViewById(R.id.button5);
        editText7 = findViewById(R.id.editText7);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPassword();
            }
        });
    }
    private void forgotPassword(){
        String email = editText7.getText().toString();
        Call<ResponseBody> call = userClient.forgetPassword(email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(ForgotPassword.this, "Token sent to email", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPassword.this,TokenOTPPage.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ForgotPassword.this, "Failed to send token", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(ForgotPassword.this, "Error sending token", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
