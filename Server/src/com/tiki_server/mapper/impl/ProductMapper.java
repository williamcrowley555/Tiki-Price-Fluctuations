package com.tiki_server.mapper.impl;

import com.tiki_server.dto.ProductDTO;
import com.tiki_server.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<ProductDTO> {
    @Override
    public ProductDTO mapRow(ResultSet rs) {
        try {
            ProductDTO product = new ProductDTO();
            product.setId(rs.getLong("id"));
            product.setSku(rs.getString("sku"));
            product.setName(rs.getString("name"));
            product.setUrlKey(rs.getString("url_key"));
            product.setUrlPath(rs.getString("url_path"));
            product.setPrice(rs.getLong("price"));
            product.setListPrice(rs.getLong("list_price"));
            product.setOriginalPrice(rs.getLong("original_price"));
            product.setDiscount(rs.getLong("discount"));
            product.setDiscountRate(rs.getInt("discount_rate"));
            product.setRatingAverage(rs.getFloat("rating_average"));
            product.setReviewCount(rs.getLong("review_count"));
            product.setFavoriteCount(rs.getLong("favourite_count"));
            product.setShortDescription(rs.getString("short_description"));
            product.setDescription(rs.getString("description"));
            product.setImageUrl(rs.getString("image_url"));
            product.setAllTimeQuantitySold(rs.getLong("all_time_quantity_sold"));
            product.setCategoryId(rs.getLong("category_id"));
            product.setBrandId(rs.getLong("brand_id"));

            return product;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
