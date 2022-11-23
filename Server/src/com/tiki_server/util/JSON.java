package com.tiki_server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsoup.Jsoup;

import java.io.IOException;

public class JSON {
    public static String get(String url) throws InterruptedException {
        String json = null;

        try {
            json = Jsoup.connect(url).ignoreContentType(true).execute().body();
        } catch (IOException e){
            return null;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectNode node = mapper.readValue(json, ObjectNode.class);
            if (node.has("status"))
                return null;
        } catch (IOException e) {
            System.out.println("Sleep for 10s");
            Thread.sleep(10000);
            return null;
        }

        return json;
    }
}
