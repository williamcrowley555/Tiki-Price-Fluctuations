package com.tiki_server.mapper.impl;

import com.tiki_server.dto.OnfigurableProductDTO;
import com.tiki_server.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OnfigurableProductMapper implements RowMapper<OnfigurableProductDTO> {
    @Override
    public OnfigurableProductDTO mapRow(ResultSet rs) {
        try {
            OnfigurableProductDTO onfigurableProduct = new OnfigurableProductDTO();
            onfigurableProduct.setId(rs.getLong("id"));
            onfigurableProduct.setChildId(rs.getLong("childId"));
            onfigurableProduct.setImageUrl(rs.getString("imageUrl"));
            onfigurableProduct.setInventoryStatus(rs.getString("inventoryStatus"));
            onfigurableProduct.setName(rs.getString("name"));
            onfigurableProduct.setOption1(rs.getString("option1"));
            onfigurableProduct.setPrice(rs.getLong("price"));
            onfigurableProduct.setSku(rs.getString("sku"));
            onfigurableProduct.setThumbnailUrl(rs.getString("thumbnailUrl"));
            onfigurableProduct.setProductId(rs.getLong("productId"));

            return onfigurableProduct;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}