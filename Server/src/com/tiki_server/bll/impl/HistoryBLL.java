package com.tiki_server.bll.impl;

import com.tiki_server.bll.IHistoryBLL;
import com.tiki_server.dal.IHistoryDAL;
import com.tiki_server.dal.impl.HistoryDAL;
import com.tiki_server.dto.HistoryDTO;
import com.tiki_server.util.InputValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.xml.validation.Validator;
import java.util.List;
import java.util.StringTokenizer;

public class HistoryBLL implements IHistoryBLL {
    private IHistoryDAL historyDAL;

    public HistoryBLL(){
        this.historyDAL = new HistoryDAL();
    }

    @Override
    public List<HistoryDTO> findAll() {
        return historyDAL.findAll();
    }

    @Override
    public List<HistoryDTO> findByProductId(Long productId) {
        return historyDAL.findByProductId(productId);
    }

    @Override
    public List<HistoryDTO> findByProductPageUrl(String url) {
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
                return findByProductId(Long.valueOf(productId));
        }

        return null;
    }

    @Override
    public HistoryDTO findById(Long id) {
        return historyDAL.findById(id);
    }

    @Override
    public Long save(HistoryDTO history) {
        return historyDAL.save(history);
    }

    @Override
    public void update(HistoryDTO history) {
        historyDAL.update(history);
    }

    @Override
    public void delete(Long id) {
        historyDAL.delete(id);
    }
}
