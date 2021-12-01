package com.tiki_server.bll.impl;

import com.tiki_server.bll.ICommentBLL;
import com.tiki_server.dal.ICommentDAL;
import com.tiki_server.dal.impl.CommentDAL;
import com.tiki_server.dto.CommentDTO;
import com.tiki_server.dto.CommentDTO;

import java.util.List;

public class CommentBLL implements ICommentBLL {
    private ICommentDAL commentDAL;

    public CommentBLL(){
        this.commentDAL = new CommentDAL();
    }

    @Override
    public List<CommentDTO> findAll() {
        return commentDAL.findAll();
    }

    @Override
    public CommentDTO findById(Long id) {
        return commentDAL.findById(id);
    }

    @Override
    public List<CommentDTO> findByReviewId(Long id) { return commentDAL.findByReviewId(id); }

    @Override
    public Long save(CommentDTO comment) {
        return commentDAL.save(comment);
    }

    @Override
    public void update(CommentDTO comment) {
        commentDAL.update(comment);
    }

    @Override
    public void delete(Long id) {
        commentDAL.delete(id);
    }
}
