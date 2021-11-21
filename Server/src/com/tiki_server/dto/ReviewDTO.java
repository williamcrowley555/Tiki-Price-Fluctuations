package com.tiki_server.dto;

public class ReviewDTO {
    private Long id;

    private String title;

    private String content;

    private String status;

    private Long commentCount;

    private int rating;

    private String imageUrl;

    private Long productId;

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
