package com.tiki_server.dal.impl;

import com.tiki_server.dal.ICommentDAL;
import com.tiki_server.dto.CommentDTO;

import java.util.List;

public class CommentDAL extends AbstractDAL<CommentDTO> implements ICommentDAL {
    @Override
    public List<CommentDTO> findAll() {
        return null;
    }

    @Override
    public CommentDTO findById(Long id) {
        return null;
    }

    @Override
    public Long save(CommentDTO comment) {
        return null;
    }

    @Override
    public void update(CommentDTO comment) {

    }

    @Override
    public void delete(Long id) {

    }
}
