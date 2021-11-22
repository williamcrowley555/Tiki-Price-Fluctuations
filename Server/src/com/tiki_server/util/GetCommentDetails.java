package com.tiki_server.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;

public class GetCommentDetails {

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
            hashMap.put("commentator",node.get("commentator").textValue());
            hashMap.put("fullname",node.get("fullname").textValue());
            hashMap.put("avatarUrl",node.get("avatar_url").textValue());
            hashMap.put("content",node.get("content").textValue());
            hashMap.put("createAt",node.get("create_at").textValue());
            hashMap.put("status",node.get("status").textValue());
            hashMap.put("isReported",node.get("is_reported").textValue());
            hashMap.put("reviewId",node.get("review_id").textValue());

            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
