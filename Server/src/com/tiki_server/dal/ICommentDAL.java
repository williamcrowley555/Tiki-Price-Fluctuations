package com.tiki_server.dal;

import com.tiki_server.dto.CommentDTO;

import java.util.List;

public interface ICommentDAL {
    List<CommentDTO> findAll();
    CommentDTO findById(Long id);
    List<CommentDTO> findByReviewId(Long id);
    Long save(CommentDTO comment);
    void update(CommentDTO comment);
    void delete(Long id);
}
