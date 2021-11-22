package com.tiki_server.dal.impl;

import com.tiki_server.dal.ICategoryDAL;
import com.tiki_server.dto.CategoryDTO;
import com.tiki_server.mapper.RowMapper;

import java.util.List;

public class CategoryDAL extends AbstractDAL<CategoryDTO> implements ICategoryDAL {
    @Override
    public List<CategoryDTO> findAll() {

        return null;
    }

    @Override
    public CategoryDTO findById(Long id) {
        return null;
    }

    @Override
    public Long save(CategoryDTO category) {
        return null;
    }

    @Override
    public void update(CategoryDTO category) {

    }

    @Override
    public void delete(Long id) {

    }
}
