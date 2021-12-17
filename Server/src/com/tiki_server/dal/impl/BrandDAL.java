package com.tiki_server.dal.impl;

import com.tiki_server.dal.IBrandDAL;
import com.tiki_server.dto.BrandDTO;
import com.tiki_server.dto.CategoryDTO;
import com.tiki_server.mapper.impl.BrandMapper;
import com.tiki_server.mapper.impl.CategoryMapper;

import java.util.List;

public class BrandDAL extends AbstractDAL<BrandDTO> implements IBrandDAL {
    @Override
    public List<BrandDTO> findAll() {
        String sql = "SELECT * FROM brand";
        return query(sql, new BrandMapper());
    }

    @Override
    public BrandDTO findById(Long id) {
        String sql = "SELECT * FROM brand WHERE id = ?";
        List<BrandDTO> brand = query(sql, new BrandMapper(), id);
        return brand.isEmpty() ? null : brand.get(0);
    }

    @Override
    public List<BrandDTO> findByCategoryId(Long categoryId) {
        String sql = "SELECT b.* FROM brand AS b, category as c, product AS p WHERE p.brand_id = b.id" +
                " AND p.category_id = c.id " +
                " AND c.id = ? " +
                " GROUP BY b.id ";
        List<BrandDTO> brand = query(sql, new BrandMapper(), categoryId);
        return brand.isEmpty() ? null : brand;
    }

    @Override
    public Long save(BrandDTO brand) {
        String sql = "INSERT INTO brand (id, name) VALUES(?, ?)";
        return insert(sql, brand.getId(), brand.getName());
    }

    @Override
    public void update(BrandDTO brand) {
        String sql = "UPDATE brand SET name = ? WHERE id = ?";
        update(sql, brand.getName(), brand.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM brand WHERE id = ?";
        update(sql, id);
    }
}
