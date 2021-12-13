package com.tiki_server.bll.impl;

import com.tiki_server.bll.IBrandBLL;
import com.tiki_server.dal.IBrandDAL;
import com.tiki_server.dal.impl.BrandDAL;
import com.tiki_server.dto.BrandDTO;

import java.util.List;

public class BrandBLL implements IBrandBLL {
    private IBrandDAL brandDAL;

    public BrandBLL() {
        this.brandDAL = new BrandDAL();
    }

    @Override
    public List<BrandDTO> findAll() {
        return brandDAL.findAll();
    }

    @Override
    public BrandDTO findById(Long id) {
        return brandDAL.findById(id);
    }

    @Override
    public List<BrandDTO> findByCategoryId(Long categoryId) { return brandDAL.findByCategoryId(categoryId); }

    @Override
    public Long save(BrandDTO brand) {
        return brandDAL.save(brand);
    }

    @Override
    public void update(BrandDTO brand) {
        brandDAL.update(brand);
    }

    @Override
    public void delete(Long id) {
        brandDAL.delete(id);
    }
}
