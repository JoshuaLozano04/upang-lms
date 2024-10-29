package com.upang.librarymanagementsystem.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;


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
        categories.add("HOSPITALITY & TOURISM");
        categories.add("Criminology");
        categories.add("ENGINEERING");

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
    private void showPopupMenu(View anchorView) {
        // Inflate the custom menu layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.custom_popup_menu, null);

        // Create the PopupWindow
        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true);

        // Set background for the PopupWindow
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setElevation(10);

        // Set up click listeners for each category
        TextView categoryAll = popupView.findViewById(R.id.category_all);
        TextView categoryHOSPITALITY = popupView.findViewById(R.id.category_HOSPITALITY);
        TextView categoryCriminology = popupView.findViewById(R.id.category_CRIMINOLOGY);
        TextView categoryEngineering = popupView.findViewById(R.id.category_engineering);

        categoryAll.setOnClickListener(v -> {
            rvBooksAdapter.filterBooksByCategory("All");
            popupWindow.dismiss();
        });

        categoryHOSPITALITY.setOnClickListener(v -> {
            rvBooksAdapter.filterBooksByCategory("hospitatlity");
            popupWindow.dismiss();
        });

        categoryCriminology.setOnClickListener(v -> {
            rvBooksAdapter.filterBooksByCategory("CRIMINOLOGY");
            popupWindow.dismiss();
        });

        categoryEngineering.setOnClickListener(v -> {
            rvBooksAdapter.filterBooksByCategory("ENGINEERING");
            popupWindow.dismiss();
        });

        // Show the PopupWindow at the desired location (anchored to the view)
        popupWindow.showAsDropDown(anchorView, 0, 0);
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
