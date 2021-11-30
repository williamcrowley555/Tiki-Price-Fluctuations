package com.tiki_server.dal;

import com.tiki_server.dto.ConfigurableProductHistoryDTO;

import java.util.List;

public interface IConfigurableProductHistoryDAL {
    List<ConfigurableProductHistoryDTO> findAll();
    List<ConfigurableProductHistoryDTO> findByProductId(Long productId, int month, int year);
    List<ConfigurableProductHistoryDTO> findByProductIdAndConfigurableProductId(Long productId, Long cpId, int month, int year);
    Long save(ConfigurableProductHistoryDTO cpHistory);
}
