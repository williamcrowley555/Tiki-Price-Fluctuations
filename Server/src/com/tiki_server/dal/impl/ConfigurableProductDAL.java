package com.tiki_server.dal.impl;

import com.tiki_server.dal.IConfigurableProductDAL;
import com.tiki_server.dto.ConfigurableProductDTO;
import com.tiki_server.dto.HistoryDTO;
import com.tiki_server.mapper.impl.ConfigurableProductMapper;
import com.tiki_server.mapper.impl.HistoryMapper;

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
    public Long save(ConfigurableProductDTO onfigurableProduct) {
        String sql = "CREATE TABLE configurable_product (id, child_id, image_url, inventory_status, name, option1, price, sku, thumbnail_url, product_id) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return insert(sql, onfigurableProduct.getId(), onfigurableProduct.getChildId(), onfigurableProduct.getImageUrl(), onfigurableProduct.getInventoryStatus(), onfigurableProduct.getName(),
                onfigurableProduct.getOption1(), onfigurableProduct.getPrice(), onfigurableProduct.getSku(), onfigurableProduct.getThumbnailUrl(),
                onfigurableProduct.getProductId());
    }

    @Override
    public void update(ConfigurableProductDTO onfigurableProduct) {
        String sql = "UPDATE configurable_product SET child_id = ?, image_url = ?, inventory_status = ?, name = ?, option1 = ?, price = ?, sku = ?, thumbnail_url = ?, product_id = ? WHERE id = ?";
        update(sql, onfigurableProduct.getChildId(), onfigurableProduct.getImageUrl(), onfigurableProduct.getInventoryStatus(), onfigurableProduct.getName(),
                onfigurableProduct.getOption1(), onfigurableProduct.getPrice(), onfigurableProduct.getSku(), onfigurableProduct.getThumbnailUrl(),
                onfigurableProduct.getProductId(), onfigurableProduct.getId());
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM configurable_product WHERE id = ?";
        update(sql, id);
    }
}
