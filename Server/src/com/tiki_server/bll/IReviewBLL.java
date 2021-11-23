package com.tiki_server.bll;

import com.tiki_server.dto.ProductDTO;
import com.tiki_server.dto.ReviewDTO;

import java.util.List;

public interface IReviewBLL {
    List<ReviewDTO> findAll();
    ReviewDTO findById(Long id);
    Long save(ReviewDTO review);
    void update(ReviewDTO review);
    void delete(Long id);
}
