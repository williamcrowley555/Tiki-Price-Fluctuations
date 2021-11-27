package com.tiki_server.dal;

import com.tiki_server.dto.ConfigurableProductHistoryDTO;

import java.util.List;

public interface IConfigurableProductHistoryDAL {
    List<ConfigurableProductHistoryDTO> findAll();
    List<ConfigurableProductHistoryDTO> findByProductId(Long productId);
    Long save(ConfigurableProductHistoryDTO cpHistory);
}
