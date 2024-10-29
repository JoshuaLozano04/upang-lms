package com.upang.librarymanagementsystem.Api.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.upang.librarymanagementsystem.Activities.BookDetails;
import com.upang.librarymanagementsystem.Api.Model.BookList;
import com.upang.librarymanagementsystem.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RvBooksAdapter extends RecyclerView.Adapter<RvBooksAdapter.ViewHolder> {

    //Initialize
    Context context;
    private ArrayList<BookList> bookLists;
    private ArrayList<BookList> filteredBookLists;
    private OkHttpClient client = new OkHttpClient();


    public RvBooksAdapter(Context context, ArrayList<BookList> arrayList){
        this.context = context;
        this.bookLists = arrayList;
        this.filteredBookLists = new ArrayList<>(arrayList);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RvBooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book,parent,false);
        return new ViewHolder(view);
    }

    //Binds data to Recycler View
    @Override
    public void onBindViewHolder(@NonNull RvBooksAdapter.ViewHolder holder, int position) {
        holder.bind(filteredBookLists.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredBookLists.size();
    }


    public void filterBooksByCategory(String category) {
        if (category.equals("All")) {
            filteredBookLists = new ArrayList<>(bookLists);
        } else {
            ArrayList<BookList> filteredBooks = new ArrayList<>();
            for (BookList book : bookLists) {
                if (book.getCategory().equalsIgnoreCase(category)) {
                    filteredBooks.add(book);
                }
            }
            filteredBookLists = filteredBooks;
        }
        notifyDataSetChanged();
    }
    //Shows the filtered books
    public void updateBookLists(ArrayList<BookList> newBookLists) {
        this.bookLists.clear();
        this.bookLists.addAll(newBookLists);
        this.filteredBookLists.clear();
        this.filteredBookLists.addAll(newBookLists);
        notifyDataSetChanged();
    }

    //Gets filter
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase().trim();
                FilterResults results = new FilterResults();

                //Checks if searchbar is empty
                if (query.isEmpty()) {
                    results.values = new ArrayList<>(bookLists); // Show all books when query is empty
                } else {
                    List<BookList> filteredList = new ArrayList<>();
                    for (BookList book : bookLists) {
                        if (book.getBookTitle().toLowerCase().contains(query)) {
                            filteredList.add(book);
                        }
                    }
                    results.values = filteredList;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredBookLists.clear();
                if (results.values != null) {
                    filteredBookLists.addAll((List<BookList>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView cover;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            cover = itemView.findViewById(R.id.iv_cover);

            //Makes the books interactable
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        BookList clickedBook = filteredBookLists.get(position);
                        int bookId = clickedBook.getId(); // Get the book ID

                        // Store the book ID in SharedPreferences
                        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("selected_book_id", bookId);
                        editor.apply();

                        // Start the DetailActivity
                        Intent intent = new Intent(context, BookDetails.class);
                        context.startActivity(intent);
                    }
                }
            });

        }
        //Binds data to holder
        public void bind(BookList bookList){
            title.setText(bookList.getBookTitle());
            String bookCoverPath = " https://decent-hardy-mastodon.ngrok-free.app/api/storage/" + bookList.getBookCover();
            fetchImage(bookCoverPath, cover);
        }
    }
    //Gets book image
    private void fetchImage(String imageUrl, ImageView imageView) {
        Request request = new Request.Builder()
                .url(imageUrl)
                .addHeader("Authorization", "Bearer " + getToken()) // Add your token here
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    byte[] imageData = response.body().bytes(); // Get the image data

                    // Load the image into the ImageView using Glide
                    ((Activity) context).runOnUiThread(() -> {
                        Glide.with(context)
                                .load(imageData)
                                .into(imageView);
                    });
                } else {
                    // Handle the error response
                    ((Activity) context).runOnUiThread(() -> {

                    });
                }
            }
        });
    }

    private String getToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("auth_token", null);
    }
}

