package com.tiki_server.bll;

import com.tiki_server.dto.ConfigurableProductHistoryDTO;
import com.tiki_server.dto.HistoryDTO;

import java.util.List;

public interface IConfigurableProductHistoryBLL {
    List<ConfigurableProductHistoryDTO> findAll();
    List<ConfigurableProductHistoryDTO> findByProductId(Long productId);
    List<ConfigurableProductHistoryDTO> findByProductPageUrl(String url);
    Long save(ConfigurableProductHistoryDTO cpHistory);
}
