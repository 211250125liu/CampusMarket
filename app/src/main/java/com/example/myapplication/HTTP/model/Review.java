package com.example.myapplication.HTTP.model;

public class Review {
    private String reviewerName;
    private String reviewText;
    private String reviewDate;
    private String reviewType; // "positive" or "negative", for example
    private String photo;

    // Constructor
    public Review(String reviewerName, String reviewText, String reviewDate, String reviewType, String photo) {
        this.reviewerName = reviewerName;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
        this.reviewType = reviewType;
        this.photo = photo;
    }

    // Getter methods
    public String getReviewerName() {
        return reviewerName;
    }

    public String getReviewText() {
        return reviewText;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public String getReviewType() {
        return reviewType;
    }

    public String getPhoto() {
        return photo;
    }
}

