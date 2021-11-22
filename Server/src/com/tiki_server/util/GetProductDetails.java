package com.tiki_server.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;

public class GetProductDetails {

    public HashMap<String, String> getProductDetails(String id)
    {
        String url = "https://tiki.vn/api/v2/products/"+id;
        Connection.Response response = null;
        try {
            response = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute();
            Document document = response.parse();
            JsonNode node = new ObjectMapper().readTree(document.text());
            HashMap<String, String> hashMap = new HashMap<String,String>();
            hashMap.put("id",node.get(id).textValue());
            hashMap.put("sku",node.get("sku").textValue());
            hashMap.put("name",node.get("name").textValue());
            hashMap.put("urlKey",node.get("url_key").textValue());
            hashMap.put("urlPath",node.get("url_path").textValue());
            hashMap.put("price",node.get("price").textValue());
            hashMap.put("listPrice",node.get("list_price").textValue());
            hashMap.put("originalPrice",node.get("original_price").textValue());
            hashMap.put("discount",node.get("discount").textValue());
            hashMap.put("discountRate",node.get("discount_rate").textValue());
            hashMap.put("ratingAverage",node.get("rating_average").textValue());
            hashMap.put("reviewCount",node.get("review_count").textValue());
            hashMap.put("favoriteCount",node.get("favorite_count").textValue());
            hashMap.put("shortDescription",node.get("short_description").textValue());
            hashMap.put("description",node.get("description").textValue());
            hashMap.put("imagesUrl",node.get("images").get("base_url").textValue());
            hashMap.put("allTimeQuantitySold",node.get("all_time_quantity_sold").textValue());
            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
