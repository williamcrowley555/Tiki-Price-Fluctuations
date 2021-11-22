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
    public ProductDTO findById(Long id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        List<ProductDTO> product = query(sql, new ProductMapper(), id);
        return product.isEmpty() ? null : product.get(0);
    }

    @Override
    public Long save(ProductDTO product) {
        String sql = "INSERT INTO product(id, all_time_quantity_sold, description, discount, discount_rate, favourite_count, image_url, list_price, name, original_price, price, rating_average, review_count, short_description, sku, url_key, url_path, category_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return insert(sql, product.getId(), product.getAllTimeQuantitySold(), product.getDescription(), product.getDiscount(), product.getDiscountRate(), product.getFavouriteCount(), product.getImageUrl(), product.getListPrice(), product.getName(), product.getOriginalPrice(), product.getPrice(), product.getRatingAverage(), product.getReviewCount(), product.getShortDescription(), product.getSku(), product.getUrlKey(), product.getUrlPath(), product.getCategoryId());
    }

    @Override
    public void update(ProductDTO product) {
        String sql = "UPDATE product SET all_time_quantity_sold = ?, description = ?, discount = ?, discount_rate = ?, favourite_count = ?, image_url = ?, "
                + "list_price = ?, name = ?, original_price = ?, price = ?, "
                + "rating_average = ?, review_count = ?, short_description = ?, sku = ?, "
                + "url_key = ?, url_path = ?, category_id = ? WHERE id = ?";
        update(sql, product.getAllTimeQuantitySold(), product.getDescription(), product.getDiscount(), product.getDiscountRate(), product.getFavouriteCount(),
                product.getImageUrl(), product.getListPrice(), product.getName(), product.getOriginalPrice(), product.getPrice(),
                product.getRatingAverage(), product.getReviewCount(), product.getShortDescription(), product.getSku(), product.getUrlKey(),
                product.getUrlPath(), product.getCategoryId(), product.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM product WHERE id = ?";
        update(sql, id);
    }
}
