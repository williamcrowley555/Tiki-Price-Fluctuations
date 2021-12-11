package com.tiki_server.dal;

import com.tiki_server.dto.ConfigurableOptionDTO;

import java.util.List;

public interface IConfigurableOptionDAL {
    List<ConfigurableOptionDTO> findAll();
    ConfigurableOptionDTO findByProductId(Long productId);
    Long save(ConfigurableOptionDTO configurableOption);
    void update(ConfigurableOptionDTO configurableOption);
}
