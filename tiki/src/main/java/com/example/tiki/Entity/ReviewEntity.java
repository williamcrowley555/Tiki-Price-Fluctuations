package com.example.tiki.Entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="review")
public class ReviewEntity {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "status")
    private String status;

    @Column(name = "comment_count")
    private Long commentCount;

    @Column(name = "rating")
    private int rating;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "full_name")
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    public ReviewEntity() {
    }
}
