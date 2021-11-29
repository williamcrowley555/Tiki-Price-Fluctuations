package com.tiki_server.dal.impl;

import com.tiki_server.dal.IHistoryDAL;
import com.tiki_server.dto.HistoryDTO;
import com.tiki_server.mapper.impl.ConfigurableProductHistoryMapper;
import com.tiki_server.mapper.impl.HistoryMapper;

import java.util.List;

public class HistoryDAL extends AbstractDAL<HistoryDTO> implements IHistoryDAL {
    @Override
    public List<HistoryDTO> findAll() {
        String sql = "SELECT * FROM history";
        return query(sql, new HistoryMapper());
    }

    @Override
    public List<HistoryDTO> findByProductId(Long productId) {
        String sql = "{CALL usp_history_getByProductId(?)}";
        return callQueryProc(sql, new HistoryMapper(), productId);
    }

    @Override
    public HistoryDTO findById(Long id) {
        String sql = "SELECT * FROM history WHERE id = ?";
        List<HistoryDTO> histories = query(sql, new HistoryMapper(), id);
        return histories.isEmpty() ? null : histories.get(0);
    }

    @Override
    public Long save(HistoryDTO history) {
        String sql = "INSERT INTO history(date, discount, discount_rate, list_price, original_price, price, product_id) VALUES(?, ?, ?, ?, ?, ?, ?)";
        return insert(sql, history.getDate(), history.getDiscount(), history.getDiscountRate(), history.getListPrice(), history.getOriginalPrice(),
                history.getPrice(),history.getProductId());
    }

    @Override
    public void update(HistoryDTO history) {
        String sql = "UPDATE history SET date = ?, discount = ?, discount_rate = ?, list_price = ?, original_price = ?, price = ?, product_id = ? WHERE id = ?";
        update(sql, history.getDate(), history.getDiscount(), history.getDiscountRate(), history.getListPrice(), history.getOriginalPrice(),
                history.getPrice(),history.getProductId(), history.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM history WHERE id = ?";
        update(sql, id);
    }
}
