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
    public ConfigurableProductDTO findById(Long id) {
        String sql = "SELECT * FROM configurable_product WHERE id = ?";
        List<ConfigurableProductDTO> configurableProduct = query(sql, new ConfigurableProductMapper(), id);
        return configurableProduct.isEmpty() ? null : configurableProduct.get(0);
    }

    @Override
    public ConfigurableProductDTO findByIdAndChildId(Long id, Long childId) {
        String sql = "SELECT * FROM configurable_product WHERE id = ? AND child_id = ?";
        List<ConfigurableProductDTO> configurableProduct = query(sql, new ConfigurableProductMapper(), id, childId);
        return configurableProduct.isEmpty() ? null : configurableProduct.get(0);
    }

    @Override
    public Long save(ConfigurableProductDTO onfigurableProduct) {
        String sql = "INSERT INTO configurable_product (id, child_id, image_url, inventory_status, name, option1, price, sku, thumbnail_url, product_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return insert(sql, onfigurableProduct.getId(), onfigurableProduct.getChildId(), onfigurableProduct.getImageUrl(), onfigurableProduct.getInventoryStatus(), onfigurableProduct.getName(),
                onfigurableProduct.getOption1(), onfigurableProduct.getPrice(), onfigurableProduct.getSku(), onfigurableProduct.getThumbnailUrl(),
                onfigurableProduct.getProductId());
    }

    @Override
    public boolean update(ConfigurableProductDTO onfigurableProduct) {
        String sql = "{CALL usp_configurable_product_update(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        return callProc(sql, onfigurableProduct.getChildId(), onfigurableProduct.getImageUrl(), onfigurableProduct.getInventoryStatus(), onfigurableProduct.getName(),
                onfigurableProduct.getOption1(), onfigurableProduct.getPrice(), onfigurableProduct.getSku(), onfigurableProduct.getThumbnailUrl(),
                onfigurableProduct.getProductId(), onfigurableProduct.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM configurable_product WHERE id = ?";
        update(sql, id);
    }

    @Override
    public boolean deleteByIdNotIn(List<Long> ids) {
        String sql = "{CALL usp_configurable_product_deleteByIdNotIn(?)}";
        String idsParam = "";

        for (Long id : ids)
            idsParam += ", " + id;

        return callProc(sql, idsParam.isEmpty() ? idsParam : idsParam.substring(2));
    }
}
