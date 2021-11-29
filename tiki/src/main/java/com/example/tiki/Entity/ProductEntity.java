package com.example.tiki.Entity;

import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="product")
public class ProductEntity implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "sku")
    private String sku;

    @Column(name = "name")
    @Nationalized
    private String name;

    @Column(name = "url_key")
    private String urlKey;

    @Column(name = "url_path")
    private String urlPath;

    @Column(name = "price")
    private Long price;

    @Column(name = "list_price")
    private Long listPrice;

    @Column(name = "original_price")
    private Long originalPrice;

    @Column(name = "discount")
    private Long discount;

    @Column(name = "discount_rate")
    private int discountRate;

    @Column(name = "rating_average")
    private float ratingAverage;

    @Column(name = "review_count")
    private Long reviewCount;

    @Column(name = "favourite_count")
    private Long favouriteCount;

    @Column(name = "short_description")
    @Nationalized
    private String shortDescription;

    @Column(name = "description")
    @Nationalized
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "all_time_quantity_sold")
    private Long allTimeQuantitySold;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    public ProductEntity() {
    }
}
