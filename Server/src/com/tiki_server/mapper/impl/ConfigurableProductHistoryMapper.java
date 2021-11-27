package com.tiki_server.mapper.impl;

import com.tiki_server.dto.ConfigurableProductHistoryDTO;
import com.tiki_server.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigurableProductHistoryMapper implements RowMapper<ConfigurableProductHistoryDTO> {
    @Override
    public ConfigurableProductHistoryDTO mapRow(ResultSet rs) {
        try {
            ConfigurableProductHistoryDTO cpHistory = new ConfigurableProductHistoryDTO();
            cpHistory.setId(rs.getLong("id"));
            cpHistory.setDate(rs.getDate("date").toLocalDate());
            cpHistory.setPrice(rs.getLong("price"));
            cpHistory.setConfigurableProductChildId(rs.getLong("configurable_product_child_id"));

            return cpHistory;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
