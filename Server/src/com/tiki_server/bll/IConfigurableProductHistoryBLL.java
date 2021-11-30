package com.tiki_server.bll;

import com.tiki_server.dto.ConfigurableProductHistoryDTO;

import java.util.List;

public interface IConfigurableProductHistoryBLL {
    List<ConfigurableProductHistoryDTO> findAll();
    List<ConfigurableProductHistoryDTO> findByProductId(Long productId, int month, int year);
    List<ConfigurableProductHistoryDTO> findByProductIdAndConfigurableProductId(Long productId, Long cpId, int month, int year);
    List<ConfigurableProductHistoryDTO> findByProductPageUrl(String url, int month, int year);
    Long save(ConfigurableProductHistoryDTO cpHistory);
}
