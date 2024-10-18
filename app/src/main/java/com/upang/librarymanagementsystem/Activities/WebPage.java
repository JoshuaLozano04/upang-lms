package com.upang.librarymanagementsystem.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.upang.librarymanagementsystem.Api.Adapter.RvBooksAdapter;
import com.upang.librarymanagementsystem.Api.Client.RetrofitClient;
import com.upang.librarymanagementsystem.Api.Interfaces.UserClient;
import com.upang.librarymanagementsystem.Api.Model.BookList;
import com.upang.librarymanagementsystem.Api.Model.BookListResponse;
import com.upang.librarymanagementsystem.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebPage extends AppCompatActivity {
    private Button accountPage;
    private UserClient userClient;
    private SearchView searchView;

    RecyclerView rv_bookdisplay;
    RvBooksAdapter rvBooksAdapter;
    ArrayList<BookList> bookLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_webpage);
        userClient = RetrofitClient.getInstance(getApplicationContext()).getApi();
        accountPage = findViewById(R.id.AccountPage);
        searchView = findViewById(R.id.searchView1);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                rvBooksAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rvBooksAdapter.getFilter().filter(newText);
                return false;
            }
        });
        accountPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WebPage.this, AccountPage.class);
                startActivity(intent);
                finish();
            }
        });

        bookLists = new ArrayList<>();
        rv_bookdisplay = findViewById(R.id.rv_bookdisplay);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(this,3);
        rv_bookdisplay.setLayoutManager(gridLayoutManager);
        rvBooksAdapter = new RvBooksAdapter(WebPage.this,bookLists);
        rv_bookdisplay.setAdapter(rvBooksAdapter);
        fetchBooks();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                rvBooksAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rvBooksAdapter.getFilter().filter(newText);
                return false;
            }
        });


    }
    private void fetchBooks(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (token != null){
            Call<BookListResponse> call = userClient.getBooks("Bearer " + token);
            call.enqueue(new Callback<BookListResponse>() {
                @Override
                public void onResponse(Call<BookListResponse> call, Response<BookListResponse> response) {
                    Log.d("API Response Code", String.valueOf(response.code())); // Log the response code
                    if (response.code() == 200){
                        if(response.isSuccessful()){
                            bookLists.clear();
                            bookLists.addAll(response.body().getData());
                            rvBooksAdapter.notifyDataSetChanged();
                            Log.d("FetchBooks", "Books fetched successfully: " + bookLists.size());
                        } else {
                        Log.d("FetchBooks", "Failed to fetch books: " + response.message());
                        }
                    }else {
                        Log.d("FetchBooks", "Failed to fetch books: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<BookListResponse> call, Throwable throwable) {

                }
            });
        }
    }
}
