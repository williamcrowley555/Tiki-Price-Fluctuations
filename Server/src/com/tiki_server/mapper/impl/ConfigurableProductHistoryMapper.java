package com.tiki_server.mapper.impl;

import com.tiki_server.dto.ConfigurableProductHistoryDTO;
import com.tiki_server.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigurableProductHistoryMapper implements RowMapper<ConfigurableProductHistoryDTO> {
    @Override
    public ConfigurableProductHistoryDTO mapRow(ResultSet rs) {
        try {
            ConfigurableProductHistoryDTO cfHistory = new ConfigurableProductHistoryDTO();
            cfHistory.setId(rs.getLong("id"));
            cfHistory.setDate(rs.getDate("date").toLocalDate());
            cfHistory.setPrice(rs.getLong("price"));
            cfHistory.setConfigurableProductId(rs.getLong("configurable_product_id"));

            return cfHistory;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
