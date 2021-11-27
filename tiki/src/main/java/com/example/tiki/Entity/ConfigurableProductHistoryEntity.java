package com.example.tiki.Entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name="configurable_product_history")
public class ConfigurableProductHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "price")
    private Long price;

    @ManyToOne
    @JoinColumn(name = "configurable_product_child_id")
    private ConfigurableProductEntity configurableProduct;

    public ConfigurableProductHistoryEntity() {
    }
}
