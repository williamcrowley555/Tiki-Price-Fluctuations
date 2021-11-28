package com.tiki_server.mapper.impl;

import com.tiki_server.dto.BrandDTO;
import com.tiki_server.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BrandMapper implements RowMapper<BrandDTO> {
    @Override
    public BrandDTO mapRow(ResultSet rs) {
        try {
            BrandDTO brand = new BrandDTO();
            brand.setId(rs.getLong("id"));
            brand.setName(rs.getString("name"));

            return brand;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
