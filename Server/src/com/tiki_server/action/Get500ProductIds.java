package com.tiki_server.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Get500ProductIds {
    public ArrayList IDS = new ArrayList();
    public ArrayList get500ProductIds(String id) {
        ArrayList arrayList = new ArrayList();
        String url = "https://tiki.vn/api/personalish/v1/blocks/listings?limit=100&categoryId=" + id + "&category="
                + id;
        for (int i = 1; i <= 5; i++) {
            String temp = url + "&page=" + i;
            Connection.Response response = null;
            try {
                response = Jsoup.connect(temp).ignoreContentType(true).method(Connection.Method.GET).execute();
                Document document = response.parse();
                if (!isValid(document.text())) {
                    return null;
                }
                JsonNode node = new ObjectMapper().readTree(document.text()).get("data");
                if(node.isArray())
                {
                    for(JsonNode objNode : node)
                    {
                        arrayList.add(objNode.get("id"));
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    private boolean isValid(String input)
    {
        String string1 = "current_page\":1,\"from\":0,\"last_page\":0,\"per_page\":1,\"to\":0,\"total\":0";
        String string2 = "\"success\\\":false";
        if(input.contains(string1) || input.contains(string2))
            return false;
        return true;
    }

    private boolean isNumber(String input)
    {
        input = input.replaceAll("[0-9]", "");
        if (input.equals(""))
            return true;
        return false;
    }

}
