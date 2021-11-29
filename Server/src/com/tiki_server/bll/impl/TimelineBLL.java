package com.tiki_server.bll.impl;

import com.tiki_server.bll.IReviewBLL;
import com.tiki_server.bll.ITimelineBLL;
import com.tiki_server.dal.IReviewDAL;
import com.tiki_server.dal.ITimelineDAL;
import com.tiki_server.dal.impl.ReviewDAL;
import com.tiki_server.dal.impl.TimelineDAL;
import com.tiki_server.dto.TimelineDTO;

import java.util.List;

public class TimelineBLL implements ITimelineBLL {
    private ITimelineDAL timelineDAL;

    public TimelineBLL() {
        this.timelineDAL = new TimelineDAL();
    }

    @Override
    public List<TimelineDTO> findAll() {
        return timelineDAL.findAll();
    }

    @Override
    public TimelineDTO findById(Long id) {
        return timelineDAL.findById(id);
    }

    @Override
    public TimelineDTO findByReviewId(Long reviewId) { return timelineDAL.findByReviewId(reviewId); }

    @Override
    public Long save(TimelineDTO timeline) {
        return timelineDAL.save(timeline);
    }

    @Override
    public void update(TimelineDTO timeline) {
        timelineDAL.update(timeline);
    }

    @Override
    public void updateByReviewId(TimelineDTO timeline) { timelineDAL.updateByReviewId(timeline); }

    @Override
    public void delete(Long id) {
        timelineDAL.delete(id);
    }
}
