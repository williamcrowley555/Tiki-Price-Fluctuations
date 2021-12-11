package com.tiki_server.dal.impl;

import com.tiki_server.dal.IConfigurableOptionDAL;
import com.tiki_server.dto.ConfigurableOptionDTO;
import com.tiki_server.mapper.impl.ConfigurableOptionMapper;

import java.util.List;

public class ConfigurableOptionDAL extends AbstractDAL<ConfigurableOptionDTO> implements IConfigurableOptionDAL {
    @Override
    public List<ConfigurableOptionDTO> findAll() {
        String sql = "SELECT * FROM configurable_option";
        return query(sql, new ConfigurableOptionMapper());
    }

    @Override
    public ConfigurableOptionDTO findByProductId(Long productId) {
        String sql = "SELECT * FROM configurable_option WHERE product_id = ?";
        List<ConfigurableOptionDTO> configurableOption = query(sql, new ConfigurableOptionMapper(), productId);
        return configurableOption.isEmpty() ? null : configurableOption.get(0);
    }

    @Override
    public Long save(ConfigurableOptionDTO configurableOption) {
        String sql = "INSERT INTO configurable_option (product_id, option_name_1, option_name_2, option_name_3) VALUES(?, ?, ?, ?)";
        return insert(sql, configurableOption.getProductId(), configurableOption.getOptionName1(), configurableOption.getOptionName2(), configurableOption.getOptionName3());
    }

    @Override
    public void update(ConfigurableOptionDTO configurableOption) {
        String sql = "UPDATE configurable_option SET option_name_1 = ?, option_name_2 = ?, option_name_3 = ? WHERE product_id = ?";
        update(sql, configurableOption.getOptionName1(), configurableOption.getOptionName2(), configurableOption.getOptionName3(), configurableOption.getProductId());
    }
}
