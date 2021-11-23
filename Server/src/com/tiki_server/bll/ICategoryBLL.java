package com.tiki_server.bll;

import com.tiki_server.dto.CategoryDTO;

import java.util.List;

public interface ICategoryBLL {
    List<CategoryDTO> findAll();
    CategoryDTO findById(Long id);
    Long save(CategoryDTO category);
    void update(CategoryDTO category);
    void delete(Long id);
}
