package com.tiki_server.bll.impl;

import com.tiki_server.bll.IConfigurableProductHistoryBLL;
import com.tiki_server.dal.IConfigurableProductHistoryDAL;
import com.tiki_server.dal.impl.ConfigurableProductHistoryDAL;
import com.tiki_server.dto.ConfigurableProductHistoryDTO;

import java.util.List;

public class ConfigurableProductHistoryBLL implements IConfigurableProductHistoryBLL {

    private IConfigurableProductHistoryDAL cpHistoryDAL;

    public ConfigurableProductHistoryBLL() {
        this.cpHistoryDAL = new ConfigurableProductHistoryDAL();
    }

    @Override
    public List<ConfigurableProductHistoryDTO> findAll() {
        return cpHistoryDAL.findAll();
    }

    @Override
    public List<ConfigurableProductHistoryDTO> findByProductId(Long productId) {
        return cpHistoryDAL.findByProductId(productId);
    }

    @Override
    public Long save(ConfigurableProductHistoryDTO cpHistory) {
        return cpHistoryDAL.save(cpHistory);
    }
}
