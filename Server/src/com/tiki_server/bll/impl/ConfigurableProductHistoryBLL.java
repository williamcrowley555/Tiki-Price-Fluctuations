package com.tiki_server.bll.impl;

import com.tiki_server.bll.IConfigurableProductHistoryBLL;
import com.tiki_server.dal.IConfigurableProductHistoryDAL;
import com.tiki_server.dal.impl.ConfigurableProductHistoryDAL;
import com.tiki_server.dto.ConfigurableProductHistoryDTO;
import com.tiki_server.util.InputValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.StringTokenizer;

public class ConfigurableProductHistoryBLL implements IConfigurableProductHistoryBLL {

    private IConfigurableProductHistoryDAL cpHistoryDAL;

    public ConfigurableProductHistoryBLL() {
        this.cpHistoryDAL = new ConfigurableProductHistoryDAL();
    }

    @Override
    public List<ConfigurableProductHistoryDTO> findAll() {
        return cpHistoryDAL.findAll();
    }

    @Override
    public List<ConfigurableProductHistoryDTO> findByProductId(Long productId, int month, int year) {
        return cpHistoryDAL.findByProductId(productId, month, year);
    }

    @Override
    public List<ConfigurableProductHistoryDTO> findByProductPageUrl(String url, int month, int year) {
        String productId = null;
        StringTokenizer stringTokenizer = new StringTokenizer(url, "-?");

        while (stringTokenizer.hasMoreTokens()) {
            String tmp = stringTokenizer.nextToken();
            if (tmp.contains(".html"))
                productId = tmp;
        }

        if (productId != null) {
            productId = StringUtils.substringBetween(productId, "p", ".html");
            if (InputValidatorUtil.isLong(productId).isEmpty())
                return findByProductId(Long.valueOf(productId), month, year);
        }

        return null;
    }

    @Override
    public Long save(ConfigurableProductHistoryDTO cpHistory) {
        return cpHistoryDAL.save(cpHistory);
    }
}
