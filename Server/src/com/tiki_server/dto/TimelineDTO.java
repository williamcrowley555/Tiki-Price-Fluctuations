package com.tiki_server.dto;

import java.util.Date;

public class TimelineDTO {
    private Long id;

    private Date reviewCreatedDate;

    private Date deliveryDate;

    private String content;

    private String explaination;

    private Long reviewId;

    public TimelineDTO() {
    }

    public TimelineDTO(Long id, Date reviewCreatedDate, Date deliveryDate, String content, String explaination, Long reviewId) {
        this.id = id;
        this.reviewCreatedDate = reviewCreatedDate;
        this.deliveryDate = deliveryDate;
        this.content = content;
        this.explaination = explaination;
        this.reviewId = reviewId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getReviewCreatedDate() {
        return reviewCreatedDate;
    }

    public void setReviewCreatedDate(Date reviewCreatedDate) {
        this.reviewCreatedDate = reviewCreatedDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExplaination() {
        return explaination;
    }

    public void setExplaination(String explaination) {
        this.explaination = explaination;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public String toString() {
        return "TimelineDTO{" +
                "id=" + id +
                ", reviewCreatedDate=" + reviewCreatedDate +
                ", deliveryDate=" + deliveryDate +
                ", content='" + content + '\'' +
                ", explaination='" + explaination + '\'' +
                ", reviewId=" + reviewId +
                '}';
    }
}
