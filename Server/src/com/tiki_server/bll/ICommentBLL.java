package com.tiki_server.bll;

import com.tiki_server.dto.CommentDTO;

import java.util.List;

public interface ICommentBLL {
    List<CommentDTO> findAll();
    CommentDTO findById(Long id);
    List<CommentDTO> findByReviewId(Long id);
    Long save(CommentDTO comment);
    void update(CommentDTO comment);
    void delete(Long id);
}
