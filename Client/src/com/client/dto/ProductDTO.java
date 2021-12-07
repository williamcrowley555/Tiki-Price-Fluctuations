package com.client.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProductDTO implements Serializable {

    private Long id;

    private String sku;

    private String name;

    private String urlKey;

    private String urlPath;

    private Long price;

    private Long listPrice;

    private Long originalPrice;

    private String description;

    private Long discount;

    private int discountRate;

    private float ratingAverage;

    private Long reviewCount;

    private Long favouriteCount;

    private String shortDescription;

    private String imageUrl;
    
    private Long allTimeQuantitySold;

    private Long categoryId = null;

    private Long brandId = null;

    public ProductDTO() {
    }

    public ProductDTO(Long id, String sku, String name, String urlKey, String urlPath, Long price, Long listPrice, Long originalPrice, String description, Long discount, int discountRate, float ratingAverage, Long reviewCount, Long favouriteCount, String shortDescription, String imageUrl, Long allTimeQuantitySold, Long categoryId, Long brandId) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.urlKey = urlKey;
        this.urlPath = urlPath;
        this.price = price;
        this.listPrice = listPrice;
        this.originalPrice = originalPrice;
        this.description = description;
        this.discount = discount;
        this.discountRate = discountRate;
        this.ratingAverage = ratingAverage;
        this.reviewCount = reviewCount;
        this.favouriteCount = favouriteCount;
        this.shortDescription = shortDescription;
        this.imageUrl = imageUrl;
        this.allTimeQuantitySold = allTimeQuantitySold;
        this.categoryId = categoryId;
        this.brandId = brandId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
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

    public int getDiscountRate() { return discountRate; }

    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }

    public float getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(float ratingAverage) {
        this.ratingAverage = ratingAverage;
    }

    public Long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Long getFavouriteCount() {
        return favouriteCount;
    }

    public void setFavoriteCount(Long favouriteCount) {
        this.favouriteCount = favouriteCount;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getAllTimeQuantitySold() {
        return allTimeQuantitySold;
    }

    public void setAllTimeQuantitySold(Long allTimeQuantitySold) {
        this.allTimeQuantitySold = allTimeQuantitySold;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public void setFavouriteCount(Long favouriteCount) { this.favouriteCount = favouriteCount; }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return discountRate == that.discountRate && Double.compare(that.ratingAverage, ratingAverage) == 0 && Objects.equals(id, that.id) && Objects.equals(sku, that.sku) && Objects.equals(name, that.name) && Objects.equals(urlKey, that.urlKey) && Objects.equals(urlPath, that.urlPath) && Objects.equals(price, that.price) && Objects.equals(listPrice, that.listPrice) && Objects.equals(originalPrice, that.originalPrice) && Objects.equals(description, that.description) && Objects.equals(discount, that.discount) && Objects.equals(reviewCount, that.reviewCount) && Objects.equals(favouriteCount, that.favouriteCount) && Objects.equals(shortDescription, that.shortDescription) && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(allTimeQuantitySold, that.allTimeQuantitySold) && Objects.equals(categoryId, that.categoryId) && Objects.equals(brandId, that.brandId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sku, name, urlKey, urlPath, price, listPrice, originalPrice, description, discount, discountRate, ratingAverage, reviewCount, favouriteCount, shortDescription, imageUrl, allTimeQuantitySold, categoryId, brandId);
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", sku='" + sku + '\'' +
                ", name='" + name + '\'' +
                ", urlKey='" + urlKey + '\'' +
                ", urlPath='" + urlPath + '\'' +
                ", price=" + price +
                ", listPrice=" + listPrice +
                ", originalPrice=" + originalPrice +
                ", description='" + description + '\'' +
                ", discount=" + discount +
                ", discountRate=" + discountRate +
                ", ratingAverage=" + ratingAverage +
                ", reviewCount=" + reviewCount +
                ", favouriteCount=" + favouriteCount +
                ", shortDescription='" + shortDescription + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", allTimeQuantitySold=" + allTimeQuantitySold +
                ", categoryId=" + categoryId +
                ", brandId=" + brandId +
                '}';
    }
}
