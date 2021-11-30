package com.tiki_server.dal.impl;

import com.tiki_server.dal.IConfigurableProductDAL;
import com.tiki_server.dto.ConfigurableProductDTO;
import com.tiki_server.dto.HistoryDTO;
import com.tiki_server.mapper.impl.ConfigurableProductMapper;
import com.tiki_server.mapper.impl.HistoryMapper;

import java.util.ArrayList;
import java.util.List;

public class ConfigurableProductDAL extends AbstractDAL<ConfigurableProductDTO> implements IConfigurableProductDAL {
    @Override
    public List<ConfigurableProductDTO> findAll() {
        String sql = "SELECT * FROM configurable_product";
        return query(sql, new ConfigurableProductMapper());
    }

    @Override
    public List<ConfigurableProductDTO> findByProductId(Long productId) {
        String sql = "SELECT * FROM configurable_product WHERE product_id = ?";
        return query(sql, new ConfigurableProductMapper(), productId);
    }

    @Override
    public ConfigurableProductDTO findByChildId(Long childId) {
        String sql = "SELECT * FROM configurable_product WHERE child_id = ?";
        List<ConfigurableProductDTO> configurableProduct = query(sql, new ConfigurableProductMapper(), childId);
        return configurableProduct.isEmpty() ? null : configurableProduct.get(0);
    }

    @Override
    public Long save(ConfigurableProductDTO configurableProduct) {
        String sql = "INSERT INTO configurable_product (child_id, image_url, inventory_status, name, option1, price, sku, thumbnail_url, product_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return insert(sql, configurableProduct.getChildId(), configurableProduct.getImageUrl(), configurableProduct.getInventoryStatus(), configurableProduct.getName(),
                configurableProduct.getOption1(), configurableProduct.getPrice(), configurableProduct.getSku(), configurableProduct.getThumbnailUrl(),
                configurableProduct.getProductId());
    }

    @Override
    public boolean update(ConfigurableProductDTO configurableProduct) {
        String sql = "{CALL usp_configurable_product_update(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        return callProc(sql, configurableProduct.getImageUrl(), configurableProduct.getInventoryStatus(), configurableProduct.getName(),
                configurableProduct.getOption1(), configurableProduct.getPrice(), configurableProduct.getSku(), configurableProduct.getThumbnailUrl(),
                configurableProduct.getProductId(), configurableProduct.getChildId());
    }

    @Override
    public void delete(Long childId) {
        String sql = "DELETE FROM configurable_product WHERE child_id = ?";
        update(sql, childId);
    }

    @Override
    public boolean deleteByIdNotIn(List<Long> ids) {
        String sql = "{CALL usp_configurable_product_deleteByChildIdNotIn(?)}";
        String idsParam = "";

        for (Long id : ids)
            idsParam += ", " + id;

        return callProc(sql, idsParam.isEmpty() ? idsParam : idsParam.substring(2));
    }
}
