package com.tiki_server.bll.impl;

import com.tiki_server.bll.IConfigurableProductBLL;
import com.tiki_server.dal.IConfigurableProductDAL;
import com.tiki_server.dal.impl.ConfigurableProductDAL;
import com.tiki_server.dto.ConfigurableProductDTO;

import java.util.List;

public class ConfigurableProductBLL implements IConfigurableProductBLL {
    private IConfigurableProductDAL configurableProductDAL;

    public ConfigurableProductBLL(){
        this.configurableProductDAL = new ConfigurableProductDAL();
    }

    @Override
    public List<ConfigurableProductDTO> findAll() {
        return configurableProductDAL.findAll();
    }

    @Override
    public List<ConfigurableProductDTO> findByProductId(Long productId) {
        return configurableProductDAL.findByProductId(productId);
    }

    @Override
    public ConfigurableProductDTO findByChildId(Long childId) {
        return configurableProductDAL.findByChildId(childId);
    }

    @Override
    public Long save(ConfigurableProductDTO configurableProduct) {
        return configurableProductDAL.save(configurableProduct);
    }

    @Override
    public boolean update(ConfigurableProductDTO configurableProduct) {
        return configurableProductDAL.update(configurableProduct);
    }

    @Override
    public void delete(Long id) {
        configurableProductDAL.delete(id);
    }

    @Override
    public boolean deleteByIdNotIn(List<Long> ids) {
        return configurableProductDAL.deleteByIdNotIn(ids);
    }
}
