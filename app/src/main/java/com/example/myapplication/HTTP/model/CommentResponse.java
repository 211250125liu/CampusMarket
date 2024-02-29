package com.example.myapplication.HTTP.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentResponse {

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
    @SerializedName("createTime")
    @Expose
    private String createTime;
    @SerializedName("reviewee")
    @Expose
    private Long reviewee;
    @SerializedName("id")
    @Expose
    private Long id;

    /**
     * No args constructor for use in serialization
     *
     */
    public CommentResponse() {
    }

    /**
     *
     * @param orderId
     * @param createTime
     * @param reviewee
     * @param reviewer
     * @param positive
     * @param id
     * @param content
     */
    public CommentResponse(Long orderId, Long reviewer, Boolean positive, String content, String createTime, Long reviewee, Long id) {
        super();
        this.orderId = orderId;
        this.reviewer = reviewer;
        this.positive = positive;
        this.content = content;
        this.createTime = createTime;
        this.reviewee = reviewee;
        this.id = id;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Long getReviewee() {
        return reviewee;
    }

    public void setReviewee(Long reviewee) {
        this.reviewee = reviewee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
