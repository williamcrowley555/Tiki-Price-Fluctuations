package com.tiki_server.dal.impl;

import com.tiki_server.dal.ICommentDAL;
import com.tiki_server.dto.CommentDTO;
import com.tiki_server.mapper.impl.CommentMapper;

import java.util.List;

public class CommentDAL extends AbstractDAL<CommentDTO> implements ICommentDAL {
    @Override
    public List<CommentDTO> findAll() {
        String sql = "SELECT * FROM comment";
        return query(sql, new CommentMapper());
    }

    @Override
    public CommentDTO findById(Long id) {
        String sql = "SELECT * FROM comment WHERE id = ?";
        List<CommentDTO> comment = query(sql, new CommentMapper(), id);
        return comment.isEmpty() ? null : comment.get(0);
    }

    @Override
    public List<CommentDTO> findByReviewId(Long id) {
        String sql = "SELECT * FROM comment WHERE review_id = ?";
        List<CommentDTO> comment = query(sql, new CommentMapper(), id);
        return comment.isEmpty() ? null : comment;
    }

    @Override
    public Long save(CommentDTO comment) {
        String sql = "INSERT INTO comment(id, avatar_url, commentator, content, create_at, fullname, is_reported, status, review_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return insert(sql, comment.getId(), comment.getAvatarUrl(), comment.getCommentator(), comment.getContent(), comment.getCreateAt(), comment.getFullname(), comment.getReported(),
                comment.getStatus(), comment.getReviewId());
    }

    @Override
    public void update(CommentDTO comment) {
        String sql = "UPDATE comment SET avatar_url = ?, commentator = ?, content = ?, create_at = ?, fullname = ?, is_reported = ?, status = ?,review_id = ? WHERE id = ?";
        update(sql, comment.getAvatarUrl(), comment.getCommentator(), comment.getContent(), comment.getCreateAt(), comment.getFullname(), comment.getReported(),
                comment.getStatus(), comment.getReviewId(), comment.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM comment WHERE id = ?";
        update(sql, id);
    }
}
