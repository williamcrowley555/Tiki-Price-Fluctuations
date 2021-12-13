package com.tiki_server.bll;

import com.tiki_server.dto.BrandDTO;

import java.util.List;

public interface IBrandBLL {
    List<BrandDTO> findAll();
    BrandDTO findById(Long id);
    List<BrandDTO> findByCategoryId(Long categoryId);
    Long save(BrandDTO brand);
    void update(BrandDTO brand);
    void delete(Long id);
}
