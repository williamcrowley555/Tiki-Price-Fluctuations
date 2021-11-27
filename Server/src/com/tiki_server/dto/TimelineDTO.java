package com.tiki_server.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimelineDTO {

    private Long id;

    @JsonProperty("review_created_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reviewCreatedDate;

    @JsonProperty("delivery_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryDate;

    @JsonProperty("content")
    private String content;

    @JsonProperty("explain")
    private String explaination;

    private Long reviewId;

//    @JsonProperty("review_created_date")
//    private void setDateReviewCreatedDate(Date reviewCreatedDate) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//        this.reviewCreatedDate = sdf.parse(String.valueOf(reviewCreatedDate));
//    }

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
