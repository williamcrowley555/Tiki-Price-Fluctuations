package com.tiki_server.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

public class TimelineDTO {

    private Long id;

    private Date reviewCreatedDate;

    private Date deliveryDate;

    private String content;

    private String explaination;

    @JsonProperty("id")
    private Long reviewId;

    @JsonProperty("timeline")
    public void unpackNested(Map<String,Object> timeline)
    {
        this.reviewCreatedDate = (Date)timeline.get("review_create_date");
        this.deliveryDate = (Date)timeline.get("delivery_date");
        this.content = (String)timeline.get("content");
        this.explaination = (String)timeline.get("explain");
    }

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
