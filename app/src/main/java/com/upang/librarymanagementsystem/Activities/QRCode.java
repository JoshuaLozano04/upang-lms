package com.upang.librarymanagementsystem.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.upang.librarymanagementsystem.Api.Client.RetrofitClient;
import com.upang.librarymanagementsystem.Api.Interfaces.UserClient;
import com.upang.librarymanagementsystem.Api.Model.ProfileResponse;
import com.upang.librarymanagementsystem.Api.Model.User;
import com.upang.librarymanagementsystem.R;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QRCode extends AppCompatActivity {
    ImageButton btnBackQR;
    Button btnCreateQR;
    private ImageView qrCodeImageView,barcodeImageView;
    private SharedPreferences sharedPreferences;
    private UserClient userClient;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qr_code);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userClient = RetrofitClient.getInstance(getApplicationContext()).getApi();

        btnBackQR = findViewById(R.id.btnBackQR);

        btnBackQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QRCode.this, AccountPage.class);
                startActivity(intent);
                finish();
            }
        });

        btnCreateQR = findViewById(R.id.btnCreateQR);

        btnCreateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchProfile();
            }
        });

        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        barcodeImageView = findViewById(R.id.barcodeImageView);
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
                            String userId = String.valueOf(user.getId());
                            generateQRCode(Integer.parseInt(userId));
                            generateBarcode(Integer.parseInt(userId));
                        } else {
                            Log.d("QR", "No user data found");
                        }
                    } else {
                        Log.d("QR", "Failed to fetch user profile: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ProfileResponse> call, Throwable throwable) {
                    Log.d("QR", "Error: " + throwable.getMessage());
                }
            });
        } else {
            Log.d("QR", "Token is null, cannot fetch profile");
        }

    }
    private void generateQRCode(int userId) {
        String qrData = "UserID:" + userId;
        try {
            BitMatrix bitMatrix = new com.google.zxing.qrcode.QRCodeWriter().encode(qrData, BarcodeFormat.QR_CODE, 200, 200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void generateBarcode(int userId) {
        // Generate a barcode using the user ID
        BitMatrix bitMatrix = new com.google.zxing.oned.Code128Writer().encode(String.valueOf(userId), BarcodeFormat.CODE_128, 400, 100);
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        barcodeImageView.setImageBitmap(bitmap);
    }
}
