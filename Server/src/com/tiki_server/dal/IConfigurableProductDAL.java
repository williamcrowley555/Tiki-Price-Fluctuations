package com.tiki_server.dal;

import com.tiki_server.dto.ConfigurableProductDTO;

import java.util.List;

public interface IConfigurableProductDAL {
    List<ConfigurableProductDTO> findAll();
    List<ConfigurableProductDTO> findByProductId(Long productId);
    ConfigurableProductDTO findByChildId(Long id);
    Long save(ConfigurableProductDTO configurableProduct);
    boolean update(ConfigurableProductDTO configurableProduct);
    void delete(Long childId);
    boolean deleteByIdNotIn(List<Long> ids);
}
