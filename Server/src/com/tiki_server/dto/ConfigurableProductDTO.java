package com.tiki_server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurableProductDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("child_id")
    private Long childId;

    private String imageUrl;

    @JsonProperty("inventory_status")
    private String inventoryStatus;

    @JsonProperty("name")
    private String name;

    @JsonProperty("option1")
    private String option1;

    @JsonProperty("price")
    private Long price;

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    private Long productId;

    @SuppressWarnings("unchecked")
    @JsonProperty("images")
    private void unpackNestedImages(List<Map<String, Object>> images) {
        try {
            this.imageUrl = (String) images.get(0).get("medium_url");
        } catch (Exception e) {
            this.imageUrl = null;
        }
    }

    public ConfigurableProductDTO() {
    }

    public ConfigurableProductDTO(Long id, Long childId, String imageUrl, String inventoryStatus, String name, String option1, Long price, String sku, String thumbnailUrl, Long productId) {
        this.id = id;
        this.childId = childId;
        this.imageUrl = imageUrl;
        this.inventoryStatus = inventoryStatus;
        this.name = name;
        this.option1 = option1;
        this.price = price;
        this.sku = sku;
        this.thumbnailUrl = thumbnailUrl;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChildId() {
        return childId;
    }

    public void setChildId(Long childId) {
        this.childId = childId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(String inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigurableProductDTO that = (ConfigurableProductDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(childId, that.childId) && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(inventoryStatus, that.inventoryStatus) && Objects.equals(name, that.name) && Objects.equals(option1, that.option1) && Objects.equals(price, that.price) && Objects.equals(sku, that.sku) && Objects.equals(thumbnailUrl, that.thumbnailUrl) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, childId, imageUrl, inventoryStatus, name, option1, price, sku, thumbnailUrl, productId);
    }

    @Override
    public String toString() {
        return "OnfigurableProductDTO{" +
                "id=" + id +
                ", childId=" + childId +
                ", imageUrl='" + imageUrl + '\'' +
                ", inventoryStatus='" + inventoryStatus + '\'' +
                ", name='" + name + '\'' +
                ", option1='" + option1 + '\'' +
                ", price=" + price +
                ", sku='" + sku + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", productId=" + productId +
                '}';
    }
}
