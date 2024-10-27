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


public class TokenOTPPage extends AppCompatActivity {

    private EditText EtEmail,EtPassword,Etpassword2,EtToken;
    private UserClient userClient;
    private Button button5;
    ImageView btnForgotBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_token_otppage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnForgotBack = findViewById(R.id.btnForgotBack);

        btnForgotBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TokenOTPPage.this, ForgotPassword.class);
                startActivity(intent);
                finish();
            }
        });
        button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
        userClient = RetrofitClient.getInstance(getApplicationContext()).getApi();
        EtEmail = findViewById(R.id.EtEmail);
        EtToken = findViewById(R.id.EtToken);
        EtPassword = findViewById(R.id.EtPassword);
        Etpassword2 = findViewById(R.id.EtPassword2);
    }
    private void resetPassword(){
        String email = EtEmail.getText().toString().trim();
        String token = EtToken.getText().toString().trim();
        String password = EtPassword.getText().toString().trim();
        String confirmPassword = Etpassword2.getText().toString().trim();

        Call<ResponseBody> call = userClient.resetPassword(email,token,password,confirmPassword);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(TokenOTPPage.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TokenOTPPage.this, LoginPage.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(TokenOTPPage.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(TokenOTPPage.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}