package com.upang.librarymanagementsystem.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Spinner;


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

    //Initializing
    private Spinner categorySpinner;
    private Button accountPage, categoryButton;
    private UserClient userClient;
    private SearchView searchView;
    private ArrayList<String> categories = new ArrayList<>();
    private RecyclerView rv_bookdisplay;
    private RvBooksAdapter rvBooksAdapter;
    private ArrayList<BookList> bookLists;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_webpage);


        userClient = RetrofitClient.getInstance(getApplicationContext()).getApi();
        accountPage = findViewById(R.id.AccountPage);
        searchView = findViewById(R.id.searchView1);

        //Categories Button
        categoryButton = findViewById(R.id.DropDown);

        categories.add("All");
        categories.add("Horror");
        categories.add("Criminology");

        categoryButton.setOnClickListener(view -> showPopupMenu(view));


        //Search Function
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


        //Account Page
        accountPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WebPage.this, AccountPage.class);
                startActivity(intent);
                finish();
            }
        });

        //Book display
        bookLists = new ArrayList<>();
        rv_bookdisplay = findViewById(R.id.rv_bookdisplay);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(this,3);
        rv_bookdisplay.setLayoutManager(gridLayoutManager);
        rvBooksAdapter = new RvBooksAdapter(WebPage.this,bookLists);
        rv_bookdisplay.setAdapter(rvBooksAdapter);
        fetchBooks();


    }

    //Button dropdown
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.category_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.category_all) {
                rvBooksAdapter.filterBooksByCategory("All");
                return true;
            } else if (item.getItemId() == R.id.category_horror) {
                rvBooksAdapter.filterBooksByCategory("Horror");
                return true;
            } else if (item.getItemId() == R.id.category_criminology) {
                rvBooksAdapter.filterBooksByCategory("Criminology");
                return true;
            } else {
                return false;
            }
        });

        // Show the menu
        popupMenu.show();
    }
    //Gets book data
    private void fetchBooks(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("auth_token", null);

        if (token != null){
            Call<BookListResponse> call = userClient.getBooks("Bearer " + token);
            call.enqueue(new Callback<BookListResponse>() {
                @Override
                public void onResponse(Call<BookListResponse> call, Response<BookListResponse> response) {
                    Log.d("API Response Code", String.valueOf(response.code()));
                    if (response.code() == 200){
                        if(response.isSuccessful()){
                            ArrayList<BookList> books = new ArrayList<>(response.body().getData());
                            rvBooksAdapter.updateBookLists(books);
                            Log.d("FetchBooks", "Books fetched successfully: " + books.size());
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
