package com.tiki_server.dal;

import com.tiki_server.dto.TimelineDTO;

import java.util.List;

public interface ITimelineDAL {
    List<TimelineDTO> findAll();
    TimelineDTO findById(Long id);
    Long save(TimelineDTO timeline);
    void update(TimelineDTO timeline);
    void delete(Long id);
}
