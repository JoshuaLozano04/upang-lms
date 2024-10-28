package com.upang.librarymanagementsystem.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.upang.librarymanagementsystem.Api.Client.RetrofitClient;
import com.upang.librarymanagementsystem.Api.Interfaces.UserClient;
import com.upang.librarymanagementsystem.Api.Model.UpdatePasswordRequest;
import com.upang.librarymanagementsystem.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends AppCompatActivity {
    ImageButton btnBackChangePass;
    private UserClient userClient;
    private EditText currentPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    private Button changePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        // Apply window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userClient = RetrofitClient.getInstance(getApplicationContext()).getApi();
        btnBackChangePass = findViewById(R.id.btnBackChangePass);

        // Initialize EditTexts
        currentPasswordEditText = findViewById(R.id.editText); // Assume this is for current password
        newPasswordEditText = findViewById(R.id.editText2); // Assume this is for new password
        confirmPasswordEditText = findViewById(R.id.editText3); // Assume this is for confirm password

        changePasswordButton = findViewById(R.id.button); // Make sure you have a button with this ID

        btnBackChangePass.setOnClickListener(view -> {
            Intent intent = new Intent(ChangePassword.this, Settings.class);
            startActivity(intent);
            finish();
        });

    }

}
