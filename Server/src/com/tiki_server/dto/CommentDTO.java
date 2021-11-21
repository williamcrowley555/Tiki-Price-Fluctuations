package com.tiki_server.dto;

public class CommentDTO {
    private Long id;

    private String commentator;

    private String fullname;

    private String avatarUrl;

    private String content;

    private Long createAt;

    private Long status;

    private Boolean isReported;

    private Long reviewId;

    public CommentDTO() {
    }

    public CommentDTO(Long id, String commentator, String fullname, String avatarUrl, String content, Long createAt, Long status, Boolean isReported, Long reviewId) {
        this.id = id;
        this.commentator = commentator;
        this.fullname = fullname;
        this.avatarUrl = avatarUrl;
        this.content = content;
        this.createAt = createAt;
        this.status = status;
        this.isReported = isReported;
        this.reviewId = reviewId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentator() {
        return commentator;
    }

    public void setCommentator(String commentator) {
        this.commentator = commentator;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Boolean getReported() {
        return isReported;
    }

    public void setReported(Boolean reported) {
        isReported = reported;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", commentator='" + commentator + '\'' +
                ", fullname='" + fullname + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", content='" + content + '\'' +
                ", createAt=" + createAt +
                ", status=" + status +
                ", isReported=" + isReported +
                ", reviewId=" + reviewId +
                '}';
    }
}
