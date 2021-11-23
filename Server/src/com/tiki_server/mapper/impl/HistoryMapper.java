package com.tiki_server.mapper.impl;

import com.tiki_server.dto.HistoryDTO;
import com.tiki_server.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoryMapper implements RowMapper<HistoryDTO> {
    @Override
    public HistoryDTO mapRow(ResultSet rs) {
        try {
            HistoryDTO history = new HistoryDTO();
            history.setId(rs.getLong("id"));
            history.setDate(rs.getDate("date").toLocalDate());
            history.setPrice(rs.getLong("price"));
            history.setListPrice(rs.getLong("listPrice"));
            history.setOriginalPrice(rs.getLong("originalPrice"));
            history.setDiscount(rs.getLong("discount"));
            history.setDiscountRate(rs.getInt("discountRate"));
            history.setProductId(rs.getLong("productId"));

            return history;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}