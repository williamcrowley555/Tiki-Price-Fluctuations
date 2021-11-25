package com.tiki_server.dal;

import com.tiki_server.dto.ConfigurableProductDTO;

import java.util.List;

public interface IConfigurableProductDAL {
    List<ConfigurableProductDTO> findAll();
    ConfigurableProductDTO findById(Long id);
    ConfigurableProductDTO findByIdAndChildId(Long id, Long childId);
    Long save(ConfigurableProductDTO onfigurableProduct);
    void update(ConfigurableProductDTO onfigurableProduct);
    void delete(Long id);
}