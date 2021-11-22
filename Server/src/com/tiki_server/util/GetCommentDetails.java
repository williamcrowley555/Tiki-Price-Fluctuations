package com.tiki_server.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GetCommentDetails {

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
                for(int j=0;j<node.get("data").get(i).get("comment").size();j++)
                {
                    hashMap = new HashMap<String,String>();
                    hashMap.put("id",node.get("data").get(i).get("comment").get(j).get("id").textValue());
                    hashMap.put("commentator",node.get("data").get(i).get("comment").get(j).get("commentator").textValue());
                    hashMap.put("fullname",node.get("data").get(i).get("comment").get(j).get("fullname").textValue());
                    hashMap.put("avatarUrl",node.get("data").get(i).get("comment").get(j).get("avatar_url").textValue());
                    hashMap.put("content",node.get("data").get(i).get("comment").get(j).get("content").textValue());
                    hashMap.put("createAt",node.get("data").get(i).get("comment").get(j).get("content").get("create_at").textValue());
                    hashMap.put("status",node.get("data").get(i).get("comment").get(j).get("content").get("status").textValue());
                    hashMap.put("isReported",node.get("data").get(i).get("comment").get(j).get("content").get("is_reported").textValue());
                    hashMap.put("reviewId",node.get("data").get(i).get("comment").get(j).get("content").get("review_id").textValue());
                    arrayList.add(hashMap);
                }
            }

            return arrayList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
