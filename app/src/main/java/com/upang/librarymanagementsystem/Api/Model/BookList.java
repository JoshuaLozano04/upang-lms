package com.upang.librarymanagementsystem.Api.Model;

import com.google.gson.annotations.SerializedName;

public class BookList {
    private int id;

    @SerializedName("Booktitle") // Ensure the names match exactly
    private String bookTitle;

    @SerializedName("Bookcover")
    private String Bookcover;
    private String Author;
    private String Bookcopies;
    private String Publisher;
    private String Description;
    private String Location;
    private String Status;
    private String Category;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookCover() {
        return Bookcover;
    }

    public void setBookCover(String Bookcover) {
        this.Bookcover = Bookcover;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getBookcopies() {
        return Bookcopies;
    }

    public void setBookcopies(String bookcopies) {
        Bookcopies = bookcopies;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
