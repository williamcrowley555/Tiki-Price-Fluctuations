package com.tiki_server.mapper.impl;

import com.tiki_server.dto.CommentDTO;
import com.tiki_server.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentMapper implements RowMapper<CommentDTO> {
    @Override
    public CommentDTO mapRow(ResultSet rs) {
        try {
            CommentDTO comment = new CommentDTO();
            comment.setId(rs.getLong("id"));
            comment.setCommentator(rs.getString("commentator"));
            comment.setFullname(rs.getString("fullname"));
            comment.setAvatarUrl(rs.getString("avatarUrl"));
            comment.setContent(rs.getString("content"));
            comment.setCreateAt(rs.getLong("createAt"));
            comment.setStatus(rs.getLong("status"));
            comment.setReported(rs.getBoolean("isReported"));
            comment.setReviewId(rs.getLong("reviewId"));

            return comment;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}