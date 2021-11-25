package com.tiki_server.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiki_server.dto.ProductDTO;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.DataInput;
import java.io.IOException;
import java.util.HashMap;

public class GetProductDetails {

    public ProductDTO getProductDetails(Long id)
    {
        String url = "https://tiki.vn/api/v2/products/"+id;
        Connection.Response response = null;
        try {
            response = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute();
            Document document = response.parse();
            JsonNode node = new ObjectMapper().readTree(document.text());
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDTO product = objectMapper.readValue(document.text(), ProductDTO.class);

            return product;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
