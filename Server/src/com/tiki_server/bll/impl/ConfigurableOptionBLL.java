package com.tiki_server.bll.impl;

import com.tiki_server.bll.IConfigurableOptionBLL;
import com.tiki_server.dal.IConfigurableOptionDAL;
import com.tiki_server.dal.impl.ConfigurableOptionDAL;
import com.tiki_server.dto.ConfigurableOptionDTO;

import java.util.List;

public class ConfigurableOptionBLL implements IConfigurableOptionBLL {
    private IConfigurableOptionDAL configurableOptionDAL;

    public ConfigurableOptionBLL() {
        this.configurableOptionDAL = new ConfigurableOptionDAL();
    }

    @Override
    public List<ConfigurableOptionDTO> findAll() {
        return configurableOptionDAL.findAll();
    }

    @Override
    public ConfigurableOptionDTO findByProductId(Long productId) {
        return configurableOptionDAL.findByProductId(productId);
    }

    @Override
    public Long save(ConfigurableOptionDTO configurableOption) {
        return configurableOptionDAL.save(configurableOption);
    }

    @Override
    public void update(ConfigurableOptionDTO configurableOption) {
        configurableOptionDAL.update(configurableOption);
    }
}
