package com.tiki_server.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;

public class GetReviewDetails {

    public HashMap<String, String> getProductDetails(String id)
    {
        String url = "https://tiki.vn/api/v2/reviews?product_id=" + id + "&include=comments&page=1&limit=-1";
        Connection.Response response = null;
        try {
            response = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute();
            Document document = response.parse();
            JsonNode node = new ObjectMapper().readTree(document.text());
            HashMap<String, String> hashMap = new HashMap<String,String>();
            hashMap.put("id",node.get("id").textValue());
            hashMap.put("title",node.get("title").textValue());
            hashMap.put("content",node.get("content").textValue());
            hashMap.put("status",node.get("status").textValue());
            hashMap.put("commentCount",node.get("comment_count").textValue());
            hashMap.put("rating",node.get("rating").textValue());
            hashMap.put("imagesUrl",node.get("images").get("full_path").textValue());
            hashMap.put("productId",node.get("product_id").textValue());

            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
