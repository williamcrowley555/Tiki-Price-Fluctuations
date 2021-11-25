package com.tiki_server.bll;

import com.tiki_server.dto.ConfigurableProductDTO;

import java.util.List;

public interface IConfigurableProductBLL {
    List<ConfigurableProductDTO> findAll();
    ConfigurableProductDTO findById(Long id);
    ConfigurableProductDTO findByIdAndChildId(Long id, Long childId);
    Long save(ConfigurableProductDTO configurableProduct);
    void update(ConfigurableProductDTO configurableProduct);
    void delete(Long id);
}
