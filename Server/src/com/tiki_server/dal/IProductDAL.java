package com.tiki_server.dal;

import com.tiki_server.dto.ProductDTO;

import java.util.List;

public interface IProductDAL {
    List<ProductDTO> findAll();
    List<ProductDTO> findByCategoryId(Long categoryId);
    List<ProductDTO> findByBrandId(Long brandId);
    ProductDTO findById(Long id);
    Long save(ProductDTO product);
    boolean update(ProductDTO product);
    void delete(Long id);
}
