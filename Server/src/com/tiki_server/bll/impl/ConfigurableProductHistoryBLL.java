package com.tiki_server.bll.impl;

import com.tiki_server.bll.IConfigurableProductBLL;
import com.tiki_server.bll.IConfigurableProductHistoryBLL;
import com.tiki_server.dal.IConfigurableProductHistoryDAL;
import com.tiki_server.dal.impl.ConfigurableProductHistoryDAL;
import com.tiki_server.dto.ConfigurableProductDTO;
import com.tiki_server.dto.ConfigurableProductHistoryDTO;
import com.tiki_server.util.InputValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.StringTokenizer;

public class ConfigurableProductHistoryBLL implements IConfigurableProductHistoryBLL {

    private IConfigurableProductHistoryDAL cpHistoryDAL;
    private IConfigurableProductBLL cpBLL;

    public ConfigurableProductHistoryBLL() {
        this.cpHistoryDAL = new ConfigurableProductHistoryDAL();
        this.cpBLL = new ConfigurableProductBLL();
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
    public List<ConfigurableProductHistoryDTO> findByProductIdAndConfigurableProductId(Long productId, Long cpId, int month, int year) {
        return cpHistoryDAL.findByProductIdAndConfigurableProductId(productId, cpId, month, year);
    }

    @Override
    public ConfigurableProductHistoryDTO findLatestByCPIdBefore(Long cpId, int month, int year) {
        return cpHistoryDAL.findLatestByCPIdBefore(cpId, month, year);
    }

    @Override
    public List<ConfigurableProductHistoryDTO> findByProductIdAndConfigurableOptions(Long productId, String option1, String option2, String option3, int month, int year) {
        ConfigurableProductDTO cp = cpBLL.findByProductIdAndOptions(productId, option1, option2, option3);

        if (cp == null)
            return null;
        else {
            List<ConfigurableProductHistoryDTO> cpHistories = findByProductIdAndConfigurableProductId(productId, cp.getChildId(), month, year);
            if (cpHistories != null && cpHistories.isEmpty()) {
                ConfigurableProductHistoryDTO latestCPHistory = findLatestByCPIdBefore(cp.getChildId(), month, year);
                return latestCPHistory == null ? null : List.of(latestCPHistory);
            }

            return cpHistories;
        }
    }

    @Override
    public List<ConfigurableProductHistoryDTO> findByProductPageUrl(String url, int month, int year) {
        boolean isValidTikiURL = InputValidatorUtil.isValidTikiURL(url).isEmpty();

        if (isValidTikiURL) {
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
        }

        return null;
    }

    @Override
    public Long save(ConfigurableProductHistoryDTO cpHistory) {
        return cpHistoryDAL.save(cpHistory);
    }
}
