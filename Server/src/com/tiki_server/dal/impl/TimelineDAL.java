package com.tiki_server.dal.impl;

import com.tiki_server.dal.IReviewDAL;
import com.tiki_server.dal.ITimelineDAL;
import com.tiki_server.dto.ReviewDTO;
import com.tiki_server.dto.TimelineDTO;
import com.tiki_server.mapper.impl.ReviewMapper;
import com.tiki_server.mapper.impl.TimelineMapper;

import java.util.List;

public class TimelineDAL extends AbstractDAL<TimelineDTO> implements ITimelineDAL {
    @Override
    public List<TimelineDTO> findAll() {
        String sql = "SELECT * FROM timeline";
        return query(sql, new TimelineMapper());
    }

    @Override
    public TimelineDTO findById(Long id) {
        String sql = "SELECT * FROM timeline WHERE id = ?";
        List<TimelineDTO> timeline = query(sql, new TimelineMapper(), id);
        return timeline.isEmpty() ? null : timeline.get(0);
    }

    @Override
    public Long save(TimelineDTO timeline) {
        String sql = "INSERT INTO timeline(id, content, delivery_date, explaination, review_created_date, review_id) VALUES(?, ?, ?, ?, ?, ?)";
        return insert(sql, timeline.getId(), timeline.getContent(), timeline.getDeliveryDate(), timeline.getExplaination(), timeline.getReviewCreatedDate(),
                timeline.getReviewId());
    }

    @Override
    public void update(TimelineDTO timeline) {
        String sql = "UPDATE timeline SET content = ?, delivery_date = ?, explaination = ?, review_created_date = ?, review_id = ? WHERE id = ?";
        update(sql, timeline.getContent(), timeline.getDeliveryDate(), timeline.getExplaination(), timeline.getReviewCreatedDate(), timeline.getReviewId(), timeline.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM timeline WHERE id = ?";
        update(sql, id);
    }
}
