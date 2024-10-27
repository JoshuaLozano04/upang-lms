package com.upang.librarymanagementsystem.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.upang.librarymanagementsystem.R;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfile extends AppCompatActivity {
    ImageButton btnBackEditProfile;
    Button button;
    EditText editText, editText2, EtEmail, editText8, editText9;
    private UserClient userClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        button = findViewById(R.id.button);
        userClient = RetrofitClient.getInstance(getApplicationContext()).getApi();
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        EtEmail = findViewById(R.id.EtEmail);
        editText8 = findViewById(R.id.editText8);
        editText9 = findViewById(R.id.editText9);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
                Log.d("Button Clicked","Button Clicked");
            }
        });
        btnBackEditProfile = findViewById(R.id.btnBackEditProfile);
        btnBackEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfile.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });


    }
    private void editProfile(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        String firstname = editText.getText().toString().trim();
        String lastname = editText2.getText().toString().trim();
        String email = EtEmail.getText().toString().trim();
        String password = editText8.getText().toString().trim();
        String confirmPassword = editText9.getText().toString().trim();

        Call<ResponseBody> call = userClient.updateProfile("Bearer " + token,firstname,lastname,email,password,confirmPassword);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(EditProfile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfile.this, LoginPage.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(EditProfile.this, "Failed to update profile: " + response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(EditProfile.this, "Error: " + throwable , Toast.LENGTH_SHORT).show();
            }
        });
    }
}