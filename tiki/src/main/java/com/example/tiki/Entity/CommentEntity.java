package com.example.tiki.Entity;

import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="comment")
public class CommentEntity implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "commentator")
    private String commentator;

    @Column(name = "fullname")
    @Nationalized
    private String fullname;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "content")
    @Nationalized
    private String content;

    @Column(name = "create_at")
    private Long createAt;

    @Column(name = "status")
    private Long status;

    @Column(name = "is_reported")
    private Boolean isReported;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private ReviewEntity review;

    public CommentEntity() {
    }
}
