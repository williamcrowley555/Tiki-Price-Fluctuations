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
