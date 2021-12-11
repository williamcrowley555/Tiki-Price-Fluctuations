package com.tiki_server.bll;

import com.tiki_server.dto.ConfigurableOptionDTO;

import java.util.List;

public interface IConfigurableOptionBLL {
    List<ConfigurableOptionDTO> findAll();
    ConfigurableOptionDTO findByProductId(Long productId);
    Long save(ConfigurableOptionDTO configurableOption);
    void update(ConfigurableOptionDTO configurableOption);
}
