package com.tiki_server.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfigurableOptionDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8898503055731705211L;

    @JsonProperty("id")
    private Long productId;

    private String optionName1;

    private String optionName2;

    private String optionName3;

    @SuppressWarnings("unchecked")
    @JsonProperty("configurable_options")
    private void unpackNestedOptionName(List<Map<String, Object>> cpOptions) {
        try {
            switch (cpOptions.size()) {
                case 0:
                    this.optionName1 = null;
                    this.optionName2 = null;
                    this.optionName3 = null;
                    break;
                case 1:
                    this.optionName1 = (String) cpOptions.get(0).get("name");
                    break;
                case 2:
                    this.optionName1 = (String) cpOptions.get(0).get("name");
                    this.optionName2 = (String) cpOptions.get(1).get("name");
                    break;
                default:
                    this.optionName1 = (String) cpOptions.get(0).get("name");
                    this.optionName2 = (String) cpOptions.get(1).get("name");
                    this.optionName3 = (String) cpOptions.get(2).get("name");
                    break;
            }
        } catch (Exception e) {
            this.optionName1 = null;
            this.optionName2 = null;
            this.optionName3 = null;
        }
    }

    public ConfigurableOptionDTO() {
    }

    public ConfigurableOptionDTO(String optionName1, String optionName2, String optionName3, Long productId) {
        this.optionName1 = optionName1;
        this.optionName2 = optionName2;
        this.optionName3 = optionName3;
        this.productId = productId;
    }

    public String getOptionName1() {
        return optionName1;
    }

    public void setOptionName1(String optionName1) {
        this.optionName1 = optionName1;
    }

    public String getOptionName2() {
        return optionName2;
    }

    public void setOptionName2(String optionName2) {
        this.optionName2 = optionName2;
    }

    public String getOptionName3() {
        return optionName3;
    }

    public void setOptionName3(String optionName3) {
        this.optionName3 = optionName3;
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
        ConfigurableOptionDTO that = (ConfigurableOptionDTO) o;
        return Objects.equals(productId, that.productId) && Objects.equals(optionName1, that.optionName1) && Objects.equals(optionName2, that.optionName2) && Objects.equals(optionName3, that.optionName3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, optionName1, optionName2, optionName3);
    }

    @Override
    public String toString() {
        return "ConfigurableOptionDTO{" +
                "productId=" + productId +
                ", optionName1='" + optionName1 + '\'' +
                ", optionName2='" + optionName2 + '\'' +
                ", optionName3='" + optionName3 + '\'' +
                '}';
    }
}
