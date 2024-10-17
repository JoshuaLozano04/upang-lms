package com.upang.librarymanagementsystem.Api.Model;

public class Books {
    private int id;
    private String Author; // Ensure that the field names match the JSON keys
    private String Booktitle; // JSON key: "Booktitle"
    private int Bookcopies; // JSON key: "Bookcopies"
    private String Publisher; // JSON key: "Publisher"
    private String Description; // JSON key: "Description"
    private String Bookcover; // JSON key: "Bookcover"
    private String Location; // JSON key: "Location"
    private int Status; // JSON key: "Status" (0 or 1)
    private String Category; // JSON key: "Category"
    private int user_id; // This is optional based on your JSON structure

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getBooktitle() {
        return Booktitle;
    }

    public void setBooktitle(String booktitle) {
        Booktitle = booktitle;
    }

    public int getBookcopies() {
        return Bookcopies;
    }

    public void setBookcopies(int bookcopies) {
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

    public String getBookcover() {
        return Bookcover;
    }

    public void setBookcover(String bookcover) {
        Bookcover = bookcover;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }
}
