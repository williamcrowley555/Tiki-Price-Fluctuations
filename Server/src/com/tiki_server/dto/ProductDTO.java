package com.tiki_server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("name")
    private String name;

    @JsonProperty("url_key")
    private String urlKey;

    @JsonProperty("url_path")
    private String urlPath;

    @JsonProperty("price")
    private Long price;

    @JsonProperty("list_price")
    private Long listPrice;

    @JsonProperty("original_price")
    private Long originalPrice;

    @JsonProperty("description")
    private String description;

    @JsonProperty("discount")
    private Long discount;

    @JsonProperty("discount_rate")
    private int discountRate;

    @JsonProperty("rating_average")
    private double ratingAverage;

    @JsonProperty("review_count")
    private Long reviewCount;

    @JsonProperty("favourite_count")
    private Long favouriteCount;

    @JsonProperty("short_description")
    private String shortDescription;

    private String imageUrl;

    @JsonProperty("all_time_quantity_sold")
    private Long allTimeQuantitySold;

    private Long categoryId = null;

    public ProductDTO() {
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("images")
    private void unpackNestedImages(List<Map<String, Object>> images) {
        try {
            this.imageUrl = (String) images.get(0).get("base_url");
        } catch (Exception e) {
            this.imageUrl = null;
        }
    }

    @SuppressWarnings("unchecked")
    @JsonProperty("categories")
    private void unpackNestedCategories(Map<String, Object> categories) {
        this.categoryId = Long.valueOf((int) categories.get("id"));
    }

    public ProductDTO(Long id, Long productId, String sku, String name, String urlKey, String urlPath, Long price, Long listPrice, Long originalPrice, Long discount, int discountRate, Long ratingAverage, Long reviewCount, Long favouriteCount, String shortDescription, String imageUrl, String description,Long allTimeQuantitySold, Long categoryId) {
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.urlKey = urlKey;
        this.urlPath = urlPath;
        this.price = price;
        this.listPrice = listPrice;
        this.originalPrice = originalPrice;
        this.discount = discount;
        this.discountRate = discountRate;
        this.ratingAverage = ratingAverage;
        this.reviewCount = reviewCount;
        this.favouriteCount = favouriteCount;
        this.shortDescription = shortDescription;
        this.imageUrl = imageUrl;
        this.allTimeQuantitySold = allTimeQuantitySold;
        this.categoryId = categoryId;
        this.description = description;
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

    public double getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(double ratingAverage) {
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public void setFavouriteCount(Long favouriteCount) { this.favouriteCount = favouriteCount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return Double.compare(that.ratingAverage, ratingAverage) == 0 && Objects.equals(id, that.id) && Objects.equals(sku, that.sku) && Objects.equals(name, that.name) && Objects.equals(urlKey, that.urlKey) && Objects.equals(urlPath, that.urlPath) && Objects.equals(price, that.price) && Objects.equals(listPrice, that.listPrice) && Objects.equals(originalPrice, that.originalPrice) && Objects.equals(description, that.description) && Objects.equals(discount, that.discount) && Objects.equals(discountRate, that.discountRate) && Objects.equals(reviewCount, that.reviewCount) && Objects.equals(favouriteCount, that.favouriteCount) && Objects.equals(shortDescription, that.shortDescription) && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(allTimeQuantitySold, that.allTimeQuantitySold) && Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sku, name, urlKey, urlPath, price, listPrice, originalPrice, description, discount, discountRate, ratingAverage, reviewCount, favouriteCount, shortDescription, imageUrl, allTimeQuantitySold, categoryId);
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
                ", discount=" + discount +
                ", discountRate=" + discountRate +
                ", ratingAverage=" + ratingAverage +
                ", reviewCount=" + reviewCount +
                ", favouriteCount=" + favouriteCount +
                ", shortDescription='" + shortDescription + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", allTimeQuantitySold=" + allTimeQuantitySold +
                ", categoryId=" + categoryId +
                '}';
    }
}
