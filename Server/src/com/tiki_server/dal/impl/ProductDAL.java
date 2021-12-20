package com.tiki_server.dal.impl;

import com.tiki_server.dal.IProductDAL;
import com.tiki_server.dto.ProductDTO;
import com.tiki_server.mapper.impl.ProductMapper;

import java.util.List;

public class ProductDAL extends AbstractDAL<ProductDTO> implements IProductDAL {
    @Override
    public List<ProductDTO> findAll() {
        String sql = "SELECT * FROM product";
        return query(sql, new ProductMapper());
    }

    @Override
    public List<ProductDTO> findByCategoryId(Long categoryId) {
        String sql = "SELECT * FROM product WHERE category_id = ?";
        return query(sql, new ProductMapper(), categoryId);
    }

    @Override
    public List<ProductDTO> findByBrandId(Long brandId) {
        String sql = "SELECT * FROM product WHERE brand_id = ?";
        return query(sql, new ProductMapper(), brandId);
    }

    @Override
    public List<ProductDTO> findAdvance(String productName, Long categoryId, Long brandId, float ratingAverage, Long minPrice, Long maxPrice) {
        String sql = "CALL usp_product_advance(?, ?, ?, ?, ?, ?);";
        return query(sql, new ProductMapper(),productName, categoryId, brandId, ratingAverage, minPrice, maxPrice);
    }

    @Override
    public List<ProductDTO> filter(String productName, Long categoryId, String brandIds, float ratingAverage, Long minPrice, Long maxPrice) {
        String sql = "{CALL usp_product_filter(?, ?, ?, ?, ?, ?)}";
        return callQueryProc(sql, new ProductMapper(), productName, categoryId, brandIds, ratingAverage, minPrice, maxPrice);
    }

    @Override
    public ProductDTO findById(Long id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        List<ProductDTO> product = query(sql, new ProductMapper(), id);
        return product.isEmpty() ? null : product.get(0);
    }

    @Override
    public Long save(ProductDTO product) {
        String sql = "INSERT INTO product (id, all_time_quantity_sold, discount, discount_rate, favourite_count, image_url, list_price, name, original_price, price, rating_average, review_count, description,short_description, sku, url_key, url_path, category_id, brand_id) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return insert(sql, product.getId(), product.getAllTimeQuantitySold(), product.getDiscount(), product.getDiscountRate(),
                product.getFavouriteCount(), product.getImageUrl(), product.getListPrice(), product.getName(),
                product.getOriginalPrice(), product.getPrice(), product.getRatingAverage(), product.getReviewCount(),
                product.getDescription(),product.getShortDescription(), product.getSku(), product.getUrlKey(),
                product.getUrlPath(), product.getCategoryId(), product.getBrandId());
    }

    @Override
    public boolean update(ProductDTO product) {
        String sql = "{CALL usp_product_update(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        return callProc(sql, product.getAllTimeQuantitySold(), product.getDiscount(), product.getDiscountRate(), product.getFavouriteCount(),
                product.getImageUrl(), product.getListPrice(), product.getName(), product.getOriginalPrice(), product.getPrice(),
                product.getRatingAverage(), product.getReviewCount(), product.getDescription(), product.getShortDescription(), product.getSku(), product.getUrlKey(),
                product.getUrlPath(), product.getCategoryId(), product.getBrandId(), product.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM product WHERE id = ?";
        update(sql, id);
    }
}
