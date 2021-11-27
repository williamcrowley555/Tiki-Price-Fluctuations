package com.tiki_server.mapper.impl;

import com.tiki_server.dto.ConfigurableProductDTO;
import com.tiki_server.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigurableProductMapper implements RowMapper<ConfigurableProductDTO> {
    @Override
    public ConfigurableProductDTO mapRow(ResultSet rs) {
        try {
            ConfigurableProductDTO onfigurableProduct = new ConfigurableProductDTO();
            onfigurableProduct.setId(rs.getLong("id"));
            onfigurableProduct.setChildId(rs.getLong("child_id"));
            onfigurableProduct.setImageUrl(rs.getString("image_url"));
            onfigurableProduct.setInventoryStatus(rs.getString("inventory_status"));
            onfigurableProduct.setName(rs.getString("name"));
            onfigurableProduct.setOption1(rs.getString("option1"));
            onfigurableProduct.setPrice(rs.getLong("price"));
            onfigurableProduct.setSku(rs.getString("sku"));
            onfigurableProduct.setThumbnailUrl(rs.getString("thumbnail_url"));
            onfigurableProduct.setProductId(rs.getLong("product_id"));

            return onfigurableProduct;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}