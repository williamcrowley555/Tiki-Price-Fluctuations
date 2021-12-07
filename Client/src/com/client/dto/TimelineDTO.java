package com.client.dto;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class TimelineDTO implements Serializable {

    private static final long serialVersionUID = -8030615438225369173L;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimelineDTO that = (TimelineDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(reviewCreatedDate, that.reviewCreatedDate) && Objects.equals(deliveryDate, that.deliveryDate) && Objects.equals(content, that.content) && Objects.equals(explaination, that.explaination) && Objects.equals(reviewId, that.reviewId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reviewCreatedDate, deliveryDate, content, explaination, reviewId);
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
