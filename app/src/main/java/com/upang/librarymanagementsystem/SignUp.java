package com.upang.librarymanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class SignUp extends AppCompatActivity {

    EditText EditTextEtPassword , EditTextEtEmail, EditTextEtName;
    Button ButtonbtnSignUp;
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

        EditTextEtEmail = findViewById(R.id.EtEmail);
        EditTextEtPassword = findViewById(R.id.EtPassword);
        EditTextEtName = findViewById(R.id.EtName);
        ButtonbtnSignUp = findViewById(R.id.btnSignUp);
        TvLogin = findViewById(R.id.TvLogin);

        ButtonbtnSignUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               String fullname, email, password;
               fullname = EditTextEtName.getText().toString();
               email = EditTextEtEmail.getText().toString();
               password = EditTextEtPassword.getText().toString();

               if(!fullname.equals("") && !email.equals("") && !password.equals("")){
                   Handler handler = new Handler(Looper.getMainLooper());
                   handler.post(new Runnable() {
                       @Override
                       public void run() {
                           String[] field = new String[3];
                           field[0] = "fullname";
                           field[1] = "email";
                           field[2] = "password";
                           String[] data = new String[3];
                           data[0] = fullname;
                           data[1] = email;
                           data[2] = password;
                           PutData putData = new PutData("http://192.168.77.16/loginregister/signup.php", "POST", field, data);
                           if (putData.startPut()) {
                               if (putData.onComplete()) {
                                   String result = putData.getResult();
                                   if(result.equals("Sign Up Success")){
                                       Intent intent = new Intent(getApplicationContext(),Login.class);
                                       startActivity(intent);
                                       finish();
                                   }
                                   else {
                                       Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                                   }
                               }
                           }

                       }
                   });
           } else {
                   Toast.makeText(getApplicationContext(),"All Fields Required", Toast.LENGTH_SHORT).show();
               }
           }
        });
        TvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });

    }
}