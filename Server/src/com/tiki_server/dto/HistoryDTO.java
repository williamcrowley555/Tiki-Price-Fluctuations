package com.tiki_server.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class HistoryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 7760203284559785601L;

    private Long id;

    private LocalDate date;

    private Long price;

    private Long listPrice;

    private Long originalPrice;

    private Long discount;

    private int discountRate;

    private Long productId;

    public HistoryDTO() {
    }

    public HistoryDTO(Long id, LocalDate date, Long price, Long listPrice, Long originalPrice, Long discount, int discountRate, Long productId) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.listPrice = listPrice;
        this.originalPrice = originalPrice;
        this.discount = discount;
        this.discountRate = discountRate;
        this.productId = productId;
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

    public Long getListPrice() {
        return listPrice;
    }

    public void setListPrice(Long listPrice) {
        this.listPrice = listPrice;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Long originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Long getDiscount() {
        return discount;
    }

    public void setDiscount(Long discount) {
        this.discount = discount;
    }

    public int getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "HistoryDTO{" +
                "id=" + id +
                ", date=" + date +
                ", price=" + price +
                ", listPrice=" + listPrice +
                ", originalPrice=" + originalPrice +
                ", discount=" + discount +
                ", discountRate=" + discountRate +
                ", productId=" + productId +
                '}';
    }
}
