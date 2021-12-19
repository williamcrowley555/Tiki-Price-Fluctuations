package com.tiki_server.dal;

import com.tiki_server.dto.HistoryDTO;

import java.util.List;

public interface IHistoryDAL {
    List<HistoryDTO> findAll();
    List<HistoryDTO> findByProductId(Long productId, int month, int year);
    HistoryDTO findLatestByProductIdBefore(Long productId, int month, int year);
    HistoryDTO findById(Long id);
    Long save(HistoryDTO history);
    void update(HistoryDTO history);
    void delete(Long id);
}
