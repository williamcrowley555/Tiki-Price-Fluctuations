package com.client.dto;

import java.io.Serializable;
import java.util.Objects;

public class BrandDTO implements Serializable {

    private Long id;

    private String name;

    public BrandDTO() {
    }

    public BrandDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrandDTO brandDTO = (BrandDTO) o;
        return Objects.equals(id, brandDTO.id) && Objects.equals(name, brandDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "BrandDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
