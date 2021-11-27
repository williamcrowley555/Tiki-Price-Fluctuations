package com.tiki_server.dto;

import java.time.LocalDate;

public class ConfigurableProductHistoryDTO {
    private Long id;

    private LocalDate date;

    private Long price;

    private Long configurableProductId;

    public ConfigurableProductHistoryDTO() {
    }

    public ConfigurableProductHistoryDTO(LocalDate date, Long price, Long configurableProductId) {
        this.date = date;
        this.price = price;
        this.configurableProductId = configurableProductId;
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

    public Long getConfigurableProductId() {
        return configurableProductId;
    }

    public void setConfigurableProductId(Long configurableProductId) {
        this.configurableProductId = configurableProductId;
    }

    @Override
    public String toString() {
        return "ConfigurableProductHistory{" +
                "id=" + id +
                ", date=" + date +
                ", price=" + price +
                ", configurableProductId=" + configurableProductId +
                '}';
    }
}
