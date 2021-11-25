package com.tiki_server.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;

public class GetOnfigurableProductDetails {

    public HashMap<String, String> getProductDetails(String id)
    {
        String url = "https://tiki.vn/api/v2/products/"+id;
        Connection.Response response = null;
        try {
            response = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute();
            Document document = response.parse();
            JsonNode node = new ObjectMapper().readTree(document.text());
            HashMap<String, String> hashMap = new HashMap<String,String>();
            hashMap.put("id",node.get("id").textValue());
            hashMap.put("childId",node.get("child_id").textValue());
            hashMap.put("imagesUrl",node.get("images").get("large_url").textValue());
            hashMap.put("inventoryStatus",node.get("inventory_status").textValue());
            hashMap.put("name",node.get("name").textValue());
            hashMap.put("option1",node.get("option1").textValue());
            hashMap.put("price",node.get("price").textValue());
            hashMap.put("sku",node.get("sku").textValue());
            hashMap.put("thumbnailUrl",node.get("thumbnail_url").textValue());
            hashMap.put("productId",node.get("product_id").textValue());

            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
