package com.tiki_server.bll;

import com.tiki_server.dto.HistoryDTO;

import java.util.List;

public interface IHistoryBLL {
    List<HistoryDTO> findAll();
    List<HistoryDTO> findByProductId(Long productId, int month, int year);
    List<HistoryDTO> findByProductPageUrl(String url, int month, int year);
    HistoryDTO findById(Long id);
    Long save(HistoryDTO history);
    void update(HistoryDTO history);
    void delete(Long id);
}
