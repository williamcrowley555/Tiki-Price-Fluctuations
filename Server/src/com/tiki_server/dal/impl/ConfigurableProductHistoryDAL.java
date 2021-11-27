package com.tiki_server.dal.impl;

import com.tiki_server.dal.IConfigurableProductHistoryDAL;
import com.tiki_server.dto.ConfigurableProductHistoryDTO;
import com.tiki_server.dto.HistoryDTO;
import com.tiki_server.mapper.impl.ConfigurableProductHistoryMapper;

import java.util.List;

public class ConfigurableProductHistoryDAL extends AbstractDAL<HistoryDTO> implements IConfigurableProductHistoryDAL {
    @Override
    public List<ConfigurableProductHistoryDTO> findAll() {
        String sql = "SELECT * FROM configurable_product_history";
        return query(sql, new ConfigurableProductHistoryMapper());
    }

    @Override
    public List<ConfigurableProductHistoryDTO> findByProductId(Long productId) {
        String sql = "{CALL usp_configurable_product_history_getByProductId(?)}";
        return callQueryProc(sql, new ConfigurableProductHistoryMapper(), productId);
    }

    @Override
    public Long save(ConfigurableProductHistoryDTO cpHistory) {
        String sql = "INSERT INTO configurable_product_history(date, price, configurable_product_child_id) VALUES(?, ?, ?)";
        return insert(sql, cpHistory.getDate(), cpHistory.getPrice(), cpHistory.getConfigurableProductChildId());
    }
}
