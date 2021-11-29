package com.tiki_server.bll;

import com.tiki_server.dto.ReviewDTO;
import com.tiki_server.dto.TimelineDTO;

import java.util.List;

public interface ITimelineBLL {
    List<TimelineDTO> findAll();
    TimelineDTO findById(Long id);
    TimelineDTO findByReviewId(Long reviewId);
    Long save(TimelineDTO timeline);
    void update(TimelineDTO timeline);
    void updateByReviewId(TimelineDTO timeline);
    void delete(Long id);
}
