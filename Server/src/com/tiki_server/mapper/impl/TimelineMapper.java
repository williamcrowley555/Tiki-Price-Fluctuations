package com.tiki_server.mapper.impl;

import com.tiki_server.dto.ReviewDTO;
import com.tiki_server.dto.TimelineDTO;
import com.tiki_server.mapper.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TimelineMapper implements RowMapper<TimelineDTO> {
    @Override
    public TimelineDTO mapRow(ResultSet rs) {
        try {
            TimelineDTO timeline = new TimelineDTO();
            timeline.setId(rs.getLong("id"));
            timeline.setReviewCreatedDate(rs.getDate("review_created_date"));
            timeline.setDeliveryDate(rs.getDate("delivery_date"));
            timeline.setContent(rs.getString("content"));
            timeline.setExplaination(rs.getString("explaination"));
            timeline.setReviewId(rs.getLong("review_id"));

            return timeline;
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
