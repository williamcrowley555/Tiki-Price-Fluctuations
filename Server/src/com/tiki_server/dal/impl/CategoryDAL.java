package com.tiki_server.dal.impl;

import com.tiki_server.dal.ICategoryDAL;
import com.tiki_server.dto.CategoryDTO;
import com.tiki_server.dto.ReviewDTO;
import com.tiki_server.mapper.RowMapper;
import com.tiki_server.mapper.impl.CategoryMapper;
import com.tiki_server.mapper.impl.ReviewMapper;

import java.util.List;

public class CategoryDAL extends AbstractDAL<CategoryDTO> implements ICategoryDAL {
    @Override
    public List<CategoryDTO> findAll() {
        String sql = "SELECT * FROM category";
        return query(sql, new CategoryMapper());
    }

    @Override
    public CategoryDTO findById(Long id) {
        String sql = "SELECT * FROM category WHERE id = ?";
        List<CategoryDTO> category = query(sql, new CategoryMapper(), id);
        return category.isEmpty() ? null : category.get(0);
    }

    @Override
    public Long save(CategoryDTO category) {
        String sql = "INSERT INTO category (id, name) VALUES(?, ?)";
        return insert(sql, category.getId(), category.getName());
    }

    @Override
    public void update(CategoryDTO category) {
        String sql = "UPDATE category SET name = ? WHERE id = ?";
        update(sql, category.getName(), category.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM category WHERE id = ?";
        update(sql, id);
    }
}
