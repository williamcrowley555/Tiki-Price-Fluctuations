package com.tiki_server.bll;

import com.tiki_server.dto.ConfigurableProductDTO;

import java.util.List;

public interface IConfigurableProductBLL {
    List<ConfigurableProductDTO> findAll();
    List<ConfigurableProductDTO> findByProductId(Long productId);
    ConfigurableProductDTO findByChildId(Long childId);
    Long save(ConfigurableProductDTO configurableProduct);
    boolean update(ConfigurableProductDTO configurableProduct);
    void delete(Long id);
    boolean deleteByIdNotIn(List<Long> ids);
}
