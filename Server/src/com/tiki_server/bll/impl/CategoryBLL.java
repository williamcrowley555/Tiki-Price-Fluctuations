package com.tiki_server.bll.impl;

import com.tiki_server.bll.ICategoryBLL;
import com.tiki_server.dal.ICategoryDAL;
import com.tiki_server.dal.impl.CategoryDAL;
import com.tiki_server.dto.CategoryDTO;

import java.util.List;

public class CategoryBLL implements ICategoryBLL {
    private ICategoryDAL categoryDAL;

    public CategoryBLL(){
        this.categoryDAL = new CategoryDAL();
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryDAL.findAll();
    }

    @Override
    public CategoryDTO findById(Long id) {
        return categoryDAL.findById(id);
    }

    @Override
    public Long save(CategoryDTO category) {
        return categoryDAL.save(category);
    }

    @Override
    public void update(CategoryDTO category) {
        categoryDAL.update(category);
    }

    @Override
    public void delete(Long id) {
        categoryDAL.delete(id);
    }
}
