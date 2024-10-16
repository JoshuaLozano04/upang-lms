package com.upang.librarymanagementsystem.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.upang.librarymanagementsystem.Api.Client.RetrofitClient;
import com.upang.librarymanagementsystem.Api.Model.ChangePasswordRequest;
import com.upang.librarymanagementsystem.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {
    EditText etCurrentPassword, etNewPassword, etRetypePassword;
    Button btnChangePassword;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        etCurrentPassword = findViewById(R.id.etCurrentPass);
        etNewPassword = findViewById(R.id.etNewPass);
        etRetypePassword = findViewById(R.id.etRetypePass);

        btnChangePassword = findViewById(R.id.btnChangePass);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePassword();
            }
        });




    }

    private void changePassword() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);
        if (token != null){
            String currentPassword = etCurrentPassword.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String retypePassword = etRetypePassword.getText().toString().trim();

            if(currentPassword.isEmpty()){
                Toast.makeText(ChangePassword.this,"Current password required",Toast.LENGTH_LONG).show();
                return;
            }

            if(newPassword.isEmpty()){
                Toast.makeText(ChangePassword.this,"New password required",Toast.LENGTH_LONG).show();
                return;
            }

            if(retypePassword.isEmpty()){
                Toast.makeText(ChangePassword.this,"Confirm password",Toast.LENGTH_LONG).show();
                return;
            }


            ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(currentPassword, newPassword);

            Call<ResponseBody> call = RetrofitClient
                    .getInstance()
                    .getApi()
                    .updatePassword("Bearer" + token,changePasswordRequest);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ChangePassword.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChangePassword.this, "Error: Could not update password.", Toast.LENGTH_SHORT).show();
                    }            }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    Toast.makeText(ChangePassword.this, "Network error. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ChangePassword.this, "User not Authorized.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ChangePassword.this, LoginPage.class);
            startActivity(intent);
            finish();
        }

    }


}