package com.example.tiki.Entity;

import org.hibernate.annotations.Nationalized;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="timeline")
public class TimelineEntity implements Serializable {

    @Id
    private Long id;

    @Column(name = "review_created_date")
    @DateTimeFormat (pattern = "dd-MM-yyyy")
    private Date reviewCreatedDate;

    @Column(name = "delivery_date")
    @DateTimeFormat (pattern = "dd-MM-yyyy")
    private Date deliveryDate;

    @Column(name = "content")
    @Nationalized
    private String content;

    @Column(name = "explaination")
    @Nationalized
    private String explaination;

    @OneToOne
    @JoinColumn(name = "review_id")
    private ReviewEntity review;

    public TimelineEntity() {
    }
}
