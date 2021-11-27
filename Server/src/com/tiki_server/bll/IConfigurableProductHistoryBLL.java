package com.tiki_server.bll;

import com.tiki_server.dto.ConfigurableProductHistoryDTO;

import java.util.List;

public interface IConfigurableProductHistoryBLL {
    List<ConfigurableProductHistoryDTO> findAll();
    List<ConfigurableProductHistoryDTO> findByProductId(Long productId);
    Long save(ConfigurableProductHistoryDTO cpHistory);
}
