package com.tiki_server.dal.impl;

import com.tiki_server.dal.IReviewDAL;
import com.tiki_server.dto.ProductDTO;
import com.tiki_server.dto.ReviewDTO;
import com.tiki_server.mapper.impl.ProductMapper;
import com.tiki_server.mapper.impl.ReviewMapper;

import java.util.List;

public class ReviewDAL extends AbstractDAL<ReviewDTO> implements IReviewDAL {
    @Override
    public List<ReviewDTO> findAll() {
        String sql = "SELECT * FROM review";
        return query(sql, new ReviewMapper());
    }

    @Override
    public ReviewDTO findById(Long id) {
        String sql = "SELECT * FROM review WHERE id = ?";
        List<ReviewDTO> review = query(sql, new ReviewMapper(), id);
        return review.isEmpty() ? null : review.get(0);
    }

    @Override
    public List<ReviewDTO> findByProductId(Long id) {
        String sql = "SELECT * FROM review WHERE product_id = ?";
        List<ReviewDTO> review = query(sql, new ReviewMapper(), id);
        return review.isEmpty() ? null : review;
    }

    @Override
    public Long save(ReviewDTO review) {
        String sql = "SET NAMES utf8mb4; INSERT INTO review(id, comment_count, content, image_url, full_name, rating, status, title, product_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return insert(sql, review.getId(), review.getCommentCount(), review.getContent(), review.getImageUrl(), review.getFullName(), review.getRating(), review.getStatus(),
                review.getTitle(), review.getProductId());
    }

    @Override
    public void update(ReviewDTO review) {
        String sql = "SET NAMES utf8mb4; UPDATE review SET comment_count = ?, content = ?, image_url = ?, full_name = ?, rating = ?, status = ?, title = ?, product_id = ? WHERE id = ?";
        update(sql, review.getCommentCount(), review.getContent(), review.getImageUrl(), review.getFullName(), review.getRating(), review.getStatus(), review.getTitle(), review.getProductId(), review.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM review WHERE id = ?";
        update(sql, id);
    }
}
