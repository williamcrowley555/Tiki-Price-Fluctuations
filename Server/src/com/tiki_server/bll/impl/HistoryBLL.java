package com.tiki_server.bll.impl;

import com.tiki_server.bll.IHistoryBLL;
import com.tiki_server.dal.IHistoryDAL;
import com.tiki_server.dal.impl.HistoryDAL;
import com.tiki_server.dto.HistoryDTO;

import java.util.List;

public class HistoryBLL implements IHistoryBLL {
    private IHistoryDAL historyDAL;

    public HistoryBLL(){
        this.historyDAL = new HistoryDAL();
    }

    @Override
    public List<HistoryDTO> findAll() {
        return historyDAL.findAll();
    }

    @Override
    public HistoryDTO findById(Long id) {
        return historyDAL.findById(id);
    }

    @Override
    public Long save(HistoryDTO history) {
        return historyDAL.save(history);
    }

    @Override
    public void update(HistoryDTO history) {
        historyDAL.update(history);
    }

    @Override
    public void delete(Long id) {
        historyDAL.delete(id);
    }
}
