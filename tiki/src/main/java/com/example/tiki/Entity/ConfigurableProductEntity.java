package com.example.tiki.Entity;


import org.hibernate.annotations.Nationalized;
import org.hibernate.mapping.Set;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="configurable_product")
public class ConfigurableProductEntity implements Serializable {
    @Id
    @Column(name = "child_id")
    private Long childId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "inventory_status")
    private String inventoryStatus;

    @Column(name = "name")
    @Nationalized
    private String name;

    @Column(name = "option1", columnDefinition = "TEXT")
    private String option1;

    @Column(name = "option2", columnDefinition = "TEXT")
    private String option2;

    @Column(name = "option3", columnDefinition = "TEXT")
    private String option3;

    @Column(name = "price")
    private Long price;

    @Column(name = "sku")
    private String sku;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @ManyToOne
    @JoinColumn(name="product_id")
    private ProductEntity product;

    public ConfigurableProductEntity() {
    }
}
