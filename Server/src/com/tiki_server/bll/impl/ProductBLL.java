package com.tiki_server.bll.impl;

import com.tiki_server.bll.IProductBLL;
import com.tiki_server.dal.IProductDAL;
import com.tiki_server.dal.impl.ProductDAL;
import com.tiki_server.dto.ProductDTO;

import java.util.List;

public class ProductBLL implements IProductBLL {
    private IProductDAL productDAL;

    public ProductBLL() {
        this.productDAL = new ProductDAL();
    }

    @Override
    public List<ProductDTO> findAll() {
        return productDAL.findAll();
    }

    @Override
    public List<ProductDTO> findByCategoryId(Long categoryId) {
        return productDAL.findByCategoryId(categoryId);
    }

    @Override
    public List<ProductDTO> findByBrandId(Long brandId) {
        return productDAL.findByBrandId(brandId);
    }

    @Override
    public List<ProductDTO> filter(String productName, Long categoryId, Long brandId, float ratingAverage, Long minPrice, Long maxPrice) {
        return productDAL.filter(productName, categoryId, brandId, ratingAverage, minPrice, maxPrice);
    }

    @Override
    public ProductDTO findById(Long id) {
        return productDAL.findById(id);
    }

    @Override
    public Long save(ProductDTO product) {
        return productDAL.save(product);
    }

    @Override
    public boolean update(ProductDTO product) {
        return productDAL.update(product);
    }

    @Override
    public void delete(Long id) {
        productDAL.delete(id);
    }
}
