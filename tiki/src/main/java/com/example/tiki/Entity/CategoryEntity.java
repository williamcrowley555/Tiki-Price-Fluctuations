package com.example.tiki.Entity;


import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="category")
public class CategoryEntity implements Serializable {
    @Id
    private Long id;

    @Column(name = "name")
    @Nationalized
    private String name;

    public CategoryEntity() {
    }
}
