package com.tiki_server.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiki_server.bll.impl.ProductBLL;
import com.tiki_server.dto.ProductDTO;
import com.tiki_server.util.Get500ProductIds;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        // categoryID list: 316, 1883, 2342, 2553, 28618(so nay bi loi), 49288, 49588, 53058
        Long categoryId = 316l;
        createProductDTO(categoryId);
    }

    public static void createProductDTO(Long categoryId) {
        String ids = String.valueOf(categoryId);
        Get500ProductIds g = new Get500ProductIds();
        ArrayList list = new ArrayList();
        list = g.get500ProductIds(ids);
        for (int i = 0; i < list.size(); i++){
            String url = "https://tiki.vn/api/v2/products/"+String.valueOf(list.get(i));
            try {
                Connection.Response response = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute();
                Document document = response.parse();
                ProductDTO product = new ObjectMapper()
                        .reader(ProductDTO.class)
                        .readValue(document.text());
                if(product.getAllTimeQuantitySold()==null)
                    product.setAllTimeQuantitySold(0l);
                System.out.println("i= "+i);
                //System.out.println(product.getReviewCount());
                product.setCategoryId(categoryId);
                ProductBLL productBLL = new ProductBLL();
                productBLL.save(product);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

    }
}