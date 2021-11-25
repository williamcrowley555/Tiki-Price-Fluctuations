package com.tiki_server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jsoup.Jsoup;

import java.io.IOException;

public class JSON {
    public static String get(String url) {
        String json = null;

        try {
            json = Jsoup.connect(url).ignoreContentType(true).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            ObjectNode node = mapper.readValue(json, ObjectNode.class);
            if (node.has("status"))
                return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return json;
    }
}
