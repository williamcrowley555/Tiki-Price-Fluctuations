package com.tiki_server.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiki_server.bll.impl.CommentBLL;
import com.tiki_server.bll.impl.ProductBLL;
import com.tiki_server.bll.impl.ReviewBLL;
import com.tiki_server.dto.CommentDTO;
import com.tiki_server.dto.ProductDTO;
import com.tiki_server.dto.ReviewDTO;
import com.tiki_server.util.Get500ProductIds;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class test {

    public static void main(String[] args) {
        // categoryID list: 316, 915, 1815, 1883, 44816, 49288, 49588, 53058
        Long categoryId = 316l;
        //createProductDTO(categoryId);
        createReviewDTO(categoryId);
        //createCommentDTO(categoryId);
    }

    public static void createProductDTO(Long categoryId) {
        String ids = String.valueOf(categoryId);
        Get500ProductIds g = new Get500ProductIds();
        ArrayList list = new ArrayList();
        list = g.get500ProductIds(ids);
        if(list.size() < 500)
            System.out.println(list.size());
        else {
            for (int i = 0; i < list.size(); i++) {
                String url = "https://tiki.vn/api/v2/products/" + String.valueOf(list.get(i));
                try {
                    Connection.Response response = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute();
                    Document document = response.parse();
                    ProductDTO product = new ObjectMapper()
                            .reader(ProductDTO.class)
                            .readValue(document.text());
                    if (product.getAllTimeQuantitySold() == null)
                        product.setAllTimeQuantitySold(0l);
                    System.out.println("i= " + i);
                    //System.out.println(product.getReviewCount());
                    product.setCategoryId(categoryId);
                    ProductBLL productBLL = new ProductBLL();
                    productBLL.save(product);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void createReviewDTO(Long categoryId) {
        String ids = String.valueOf(categoryId);
        Get500ProductIds g = new Get500ProductIds();
        ArrayList list = new ArrayList();
        list = g.get500ProductIds(ids);
        for (int i = 0; i < list.size(); i++) {
            String url = "https://tiki.vn/api/v2/reviews?product_id=" + String.valueOf(list.get(i)) + "&include=comments&page=1&limit=-1";
            try {
                Connection.Response response = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute();
                Document document = response.parse();
                JsonNode node = new ObjectMapper().readTree(document.text()).get("data");
                for(JsonNode objNode1 : node)
                {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.reader(CommentDTO.class).readValue(objNode1);
                    ReviewDTO reviewDto = new ObjectMapper().reader(CommentDTO.class).readValue(objNode1);
                    System.out.println(reviewDto);
                    //reviewDto.getStatus();
                    //ReviewBLL reviewBLL = new ReviewBLL();
                    //reviewBLL.save(reviewDto);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void createCommentDTO(Long categoryId) {
        String ids = String.valueOf(categoryId);
        Get500ProductIds g = new Get500ProductIds();
        ArrayList list = new ArrayList();
        list = g.get500ProductIds(ids);
        for (int i = 0; i < list.size(); i++) {
            String url = "https://tiki.vn/api/v2/reviews?product_id=" + String.valueOf(list.get(i)) + "&include=comments&page=1&limit=-1";
            try {
                Connection.Response response = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute();
                Document document = response.parse();
                JsonNode node = new ObjectMapper().readTree(document.text()).get("data");
                for(JsonNode objNode1 : node)
                {
                    JsonNode comments =  objNode1.get("comments");
                    for(JsonNode objNode2 : comments)
                    {
                        CommentDTO commentDto = new ObjectMapper().reader(CommentDTO.class).readValue(objNode2);
                        System.out.println(commentDto.getContent());
                        System.out.println(commentDto.getAvatarUrl());
                        CommentBLL commentBLL = new CommentBLL();
                        commentBLL.save(commentDto);
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}