package com.tiki_server.dal;

import com.tiki_server.dto.HistoryDTO;

import java.util.List;

public interface IHistoryDAL {
    List<HistoryDTO> findAll();
    HistoryDTO findById(Long id);
    Long save(HistoryDTO history);
    void update(HistoryDTO history);
    void delete(Long id);
}
