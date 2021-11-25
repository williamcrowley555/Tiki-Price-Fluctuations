package com.tiki_server.bll;

import com.tiki_server.dto.ProductDTO;

import java.util.List;

public interface IProductBLL {
    List<ProductDTO> findAll();
    ProductDTO findById(Long id);
    Long save(ProductDTO product);
    void update(ProductDTO product);
    void delete(Long id);
}