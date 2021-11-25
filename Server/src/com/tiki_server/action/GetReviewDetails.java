package com.tiki_server.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GetReviewDetails {

    public ArrayList getProductDetails(String id)
    {
        String url = "https://tiki.vn/api/v2/reviews?product_id=" + id + "&include=comments&page=1&limit=-1";
        Connection.Response response = null;
        try {
            response = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute();
            Document document = response.parse();
            JsonNode node = new ObjectMapper().readTree(document.text());
            ArrayList arrayList = new ArrayList();
            HashMap<String, String> hashMap;
            for (int i=0;i<node.get("data").size();i++)
            {
                hashMap = new HashMap<String,String>();
                hashMap.put("id",node.get("data").get(i).get("id").textValue());
                hashMap.put("title",node.get("data").get(i).get("title").textValue());
                hashMap.put("content",node.get("data").get(i).get("content").textValue());
                hashMap.put("commentCount",node.get("data").get(i).get("comment_count").textValue());
                hashMap.put("rating",node.get("data").get(i).get("rating").textValue());
                hashMap.put("imageUrl",node.get("data").get(i).get("images").get(0).get("full_path").textValue());
                hashMap.put("productId",node.get("data").get(i).get("product_id").textValue());
                arrayList.add(hashMap);
            }

            return arrayList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
