package com.tiki_server.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("status")
    private String status;

    @JsonProperty("comment_count")
    private Long commentCount;

    @JsonProperty("rating")
    private int rating;

    private String imageUrl;

    @JsonProperty("product_id")
    private Long productId;

    public ReviewDTO() {
    }

    @JsonProperty("images")
    public void unpackNested(List<Map<String,Object>> images)
    {
        try {
            this.imageUrl = (String)images.get(0).get("full_path");
        } catch (Exception e)
        {
            this.imageUrl = null;
        }
    }

    public ReviewDTO(Long id) {
        this.id = id;
    }

    public ReviewDTO(Long id, String title, String content, String status, Long commentCount, int rating, String imageUrl, Long productId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = status;
        this.commentCount = commentCount;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewDTO reviewDTO = (ReviewDTO) o;
        return rating == reviewDTO.rating && Objects.equals(id, reviewDTO.id) && Objects.equals(title, reviewDTO.title) && Objects.equals(content, reviewDTO.content) && Objects.equals(status, reviewDTO.status) && Objects.equals(commentCount, reviewDTO.commentCount) && Objects.equals(imageUrl, reviewDTO.imageUrl) && Objects.equals(productId, reviewDTO.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, status, commentCount, rating, imageUrl, productId);
    }

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status='" + status + '\'' +
                ", commentCount=" + commentCount +
                ", rating=" + rating +
                ", imageUrl='" + imageUrl + '\'' +
                ", productId=" + productId +
                '}';
    }
}
