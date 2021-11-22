package com.tiki_server.mapper.impl;

import com.tiki_server.dto.CategoryDTO;
import com.tiki_server.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryMapper implements RowMapper<CategoryDTO> {
    @Override
    public CategoryDTO mapRow(ResultSet rs) {
        try {
            CategoryDTO category = new CategoryDTO();
            category.setId(rs.getLong("id"));
            category.setName(rs.getString("name"));

            return category;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
