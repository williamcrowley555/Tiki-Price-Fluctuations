package com.tiki_server.bll;

import com.tiki_server.dto.ConfigurableProductDTO;

import java.util.List;

public interface IConfigurableProductBLL {
    List<ConfigurableProductDTO> findAll();
    List<ConfigurableProductDTO> findByProductId(Long productId);
    ConfigurableProductDTO findByChildId(Long childId);
    ConfigurableProductDTO findByProductIdAndOptions(Long productId, String option1, String option2, String option3);
    Long save(ConfigurableProductDTO configurableProduct);
    boolean update(ConfigurableProductDTO configurableProduct);
    void delete(Long id);
    boolean deleteByIdNotIn(List<Long> ids);
}
