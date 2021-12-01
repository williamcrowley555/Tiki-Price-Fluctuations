package com.tiki_server.bll.impl;

import com.tiki_server.bll.IProductBLL;
import com.tiki_server.bll.IReviewBLL;
import com.tiki_server.dal.IProductDAL;
import com.tiki_server.dal.IReviewDAL;
import com.tiki_server.dal.impl.ProductDAL;
import com.tiki_server.dal.impl.ReviewDAL;
import com.tiki_server.dto.ReviewDTO;

import java.util.List;

public class ReviewBLL implements IReviewBLL {
    private IReviewDAL reviewDAL;

    public ReviewBLL() {
        this.reviewDAL = new ReviewDAL();
    }

    @Override
    public List<ReviewDTO> findAll() {
        return reviewDAL.findAll();
    }

    @Override
    public ReviewDTO findById(Long id) {
        return reviewDAL.findById(id);
    }

    @Override
    public List<ReviewDTO> findByProductId(Long id) { return reviewDAL.findByProductId(id); }

    @Override
    public Long save(ReviewDTO review) {
        return reviewDAL.save(review);
    }

    @Override
    public void update(ReviewDTO review) {
        reviewDAL.update(review);
    }

    @Override
    public void delete(Long id) {
        reviewDAL.delete(id);
    }
}
