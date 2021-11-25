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
    public ConfigurableProductDTO findById(Long id) {
        return configurableProductDAL.findById(id);
    }

    @Override
    public ConfigurableProductDTO findByIdAndChildId(Long id, Long childId) {
        return configurableProductDAL.findByIdAndChildId(id, childId);
    }

    @Override
    public Long save(ConfigurableProductDTO configurableProduct) {
        return configurableProductDAL.save(configurableProduct);
    }

    @Override
    public void update(ConfigurableProductDTO configurableProduct) {
        configurableProductDAL.update(configurableProduct);
    }

    @Override
    public void delete(Long id) {
        configurableProductDAL.delete(id);
    }
}
