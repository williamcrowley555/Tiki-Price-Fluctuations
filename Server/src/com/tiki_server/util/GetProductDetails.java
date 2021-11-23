package com.tiki_server.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;

public class GetProductDetails {

    public HashMap<String, Object> getProductDetails(Long id)
    {
        String url = "https://tiki.vn/api/v2/products/"+id;
        Connection.Response response = null;
        try {
            response = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute();
            Document document = response.parse();
            JsonNode node = new ObjectMapper().readTree(document.text());
            HashMap<String, Object> hashMap = new HashMap<String,Object>();
            hashMap.put("id",node.get("id"));
            hashMap.put("sku",node.get("sku"));
            hashMap.put("name",node.get("name"));
            hashMap.put("urlKey",node.get("url_key"));
            hashMap.put("urlPath",node.get("url_path"));
            hashMap.put("price",node.get("price"));
            hashMap.put("listPrice",node.get("list_price"));
            hashMap.put("originalPrice",node.get("original_price"));
            hashMap.put("discount",node.get("discount"));
            hashMap.put("discountRate",node.get("discount_rate"));
            hashMap.put("ratingAverage",node.get("rating_average"));
            hashMap.put("reviewCount",node.get("review_count"));
            hashMap.put("favoriteCount",node.get("favourite_count"));
            hashMap.put("shortDescription",node.get("short_description"));
            hashMap.put("description",node.get("description"));
            hashMap.put("imagesUrl",node.get("images").get(0).get("base_url"));
            hashMap.put("allTimeQuantitySold",node.get("all_time_quantity_sold"));
            //System.out.println(hashMap);
            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
