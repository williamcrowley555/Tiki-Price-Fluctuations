package com.example.tiki.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name="history")
public class HistoryEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "date")
    private LocalDate date;

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

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    public HistoryEntity() {
    }
}
