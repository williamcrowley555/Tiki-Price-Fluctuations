package com.tiki_server.dal;

import com.tiki_server.dto.OnfigurableProductDTO;

import java.util.List;

public interface IOnfigurableProductDAL {
    List<OnfigurableProductDTO> findAll();
    OnfigurableProductDTO findById(Long id);
    Long save(OnfigurableProductDTO onfigurableProduct);
    void update(OnfigurableProductDTO onfigurableProduct);
    void delete(Long id);
}
