package com.tiki_server.mapper.impl;

import com.tiki_server.dto.ConfigurableOptionDTO;
import com.tiki_server.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConfigurableOptionMapper implements RowMapper<ConfigurableOptionDTO> {
    @Override
    public ConfigurableOptionDTO mapRow(ResultSet rs) {
        try {
            ConfigurableOptionDTO configurableOption = new ConfigurableOptionDTO();
            configurableOption.setProductId(rs.getLong("product_id"));
            configurableOption.setOptionName1(rs.getString("option_name_1"));
            configurableOption.setOptionName2(rs.getString("option_name_2"));
            configurableOption.setOptionName3(rs.getString("option_name_3"));

            return configurableOption;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
