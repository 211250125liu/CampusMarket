package com.example.myapplication.HTTP.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentRequest {

    @SerializedName("orderId")
    @Expose
    private Long orderId;
    @SerializedName("reviewer")
    @Expose
    private Long reviewer;
    @SerializedName("positive")
    @Expose
    private Boolean positive;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("reviewee")
    @Expose
    private Long reviewee;

    /**
     * No args constructor for use in serialization
     */
    public CommentRequest() {
    }

    /**
     * @param orderId
     * @param reviewee
     * @param reviewer
     * @param positive
     * @param content
     */
    public CommentRequest(Long orderId, Long reviewer, Long reviewee, Boolean positive, String content) {
        super();
        this.orderId = orderId;
        this.reviewer = reviewer;
        this.positive = positive;
        this.content = content;
        this.reviewee = reviewee;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getReviewer() {
        return reviewer;
    }

    public void setReviewer(Long reviewer) {
        this.reviewer = reviewer;
    }

    public Boolean getPositive() {
        return positive;
    }

    public void setPositive(Boolean positive) {
        this.positive = positive;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getReviewee() {
        return reviewee;
    }

    public void setReviewee(Long reviewee) {
        this.reviewee = reviewee;
    }

}