package com.example.tiki.Entity;

import javax.persistence.*;

@Entity
@Table(name = "brand")
public class BrandEntity {
    @Id
    private Long id;

    @Column(name = "name")
    private String name;

    public BrandEntity() {
    }
}
