package com.example.tiki.Entity;

import javax.persistence.*;

@Entity
@Table(name = "configurable_option")
public class ConfigurableOptionEntity {
    @Id
    private Long productId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "option_name_1")
    private String optionName1;

    @Column(name = "option_name_2")
    private String optionName2;

    @Column(name = "option_name_3")
    private String optionName3;

}
