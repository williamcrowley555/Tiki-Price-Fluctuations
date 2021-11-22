package com.tiki_server.mapper.impl;

import com.tiki_server.dto.ProductDTO;
import com.tiki_server.dto.ReviewDTO;
import com.tiki_server.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewMapper implements RowMapper<ReviewDTO> {

    @Override
    public ReviewDTO mapRow(ResultSet rs) {
        try {
            ReviewDTO review = new ReviewDTO();
            review.setId(rs.getLong("id"));
            review.setTitle(rs.getString("title"));
            review.setContent(rs.getString("content"));
            review.setStatus(rs.getString("status"));
            review.setCommentCount(rs.getLong("comment_count"));
            review.setRating(rs.getInt("rating"));
            review.setImageUrl(rs.getString("image_url"));
            review.setProductId(rs.getLong("product_id"));

            return review;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
