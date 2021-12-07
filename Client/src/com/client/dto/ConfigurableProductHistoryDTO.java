package com.client.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class ConfigurableProductHistoryDTO implements Serializable {

    private Long id;

    private LocalDate date;

    private Long price;

    private Long configurableProductChildId;

    public ConfigurableProductHistoryDTO() {
    }

    public ConfigurableProductHistoryDTO(LocalDate date, Long price, Long configurableProductChildId) {
        this.date = date;
        this.price = price;
        this.configurableProductChildId = configurableProductChildId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getConfigurableProductChildId() {
        return configurableProductChildId;
    }

    public void setConfigurableProductChildId(Long configurableProductChildId) {
        this.configurableProductChildId = configurableProductChildId;
    }

    @Override
    public String toString() {
        return "ConfigurableProductHistoryDTO{" +
                "id=" + id +
                ", date=" + date +
                ", price=" + price +
                ", configurableProductChildId=" + configurableProductChildId +
                '}';
    }
}
