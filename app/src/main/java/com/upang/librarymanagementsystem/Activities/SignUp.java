package com.upang.librarymanagementsystem.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.upang.librarymanagementsystem.Api.Client.RetrofitClient;
import com.upang.librarymanagementsystem.R;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    EditText EtFirstname, EtLastname, EtEmail, EtPassword1, EtPassword2 ;
    Button btnSignUp;
    TextView TvLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EtEmail = findViewById(R.id.EtEmail);
        EtFirstname = findViewById(R.id.EtFirstname);
        EtLastname = findViewById(R.id.EtLastname);
        EtPassword1 = findViewById(R.id.EtPassword);
        EtPassword2 = findViewById(R.id.EtPassword2);
        TvLogin = findViewById(R.id.TvLogin);
        btnSignUp = findViewById(R.id.btnSignUp);

        TvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }
    private void signUp(){
        String email = EtEmail.getText().toString().trim();
        String firstname = EtFirstname.getText().toString().trim();
        String lastname = EtLastname.getText().toString().trim();
        String password = EtPassword1.getText().toString().trim();
        String password2 = EtPassword2.getText().toString().trim();

        if(email.isEmpty()){
            Toast.makeText(SignUp.this,"Email is required",Toast.LENGTH_LONG).show();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(SignUp.this,"Enter valid email",Toast.LENGTH_LONG).show();
            return;
        }
        if(firstname.isEmpty()){
            Toast.makeText(SignUp.this,"Firstname is required",Toast.LENGTH_LONG).show();
            return;
        }
        if(lastname.isEmpty()){
            Toast.makeText(SignUp.this,"Lastname is required",Toast.LENGTH_LONG).show();
            return;
        }
        if(password.isEmpty()){
            Toast.makeText(SignUp.this,"Password is required",Toast.LENGTH_LONG).show();
            return;
        }
        if(password2 != password){
            Toast.makeText(SignUp.this,"Password must match",Toast.LENGTH_LONG).show();
            return;
        }
        if(password.length() < 8){
            Toast.makeText(SignUp.this,"Password should be 8 characters long",Toast.LENGTH_LONG).show();
            return;
        }

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .register(firstname,lastname,email,password);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String s = response.body().string();
                        Toast.makeText(SignUp.this, s, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(SignUp.this, "Response is not successful or body is null", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Toast.makeText(SignUp.this,throwable.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}