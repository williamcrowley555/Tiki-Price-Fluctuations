package com.tiki_server.dal;

import com.tiki_server.dto.CategoryDTO;

import java.util.List;

public interface ICategoryDAL {
    List<CategoryDTO> findAll();
    CategoryDTO findById(Long id);
    Long save(CategoryDTO category);
    void update(CategoryDTO category);
    void delete(Long id);
}
