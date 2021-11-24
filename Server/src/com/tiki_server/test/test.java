package com.tiki_server.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiki_server.bll.impl.*;
import com.tiki_server.dto.*;
import com.tiki_server.util.Get500ProductIds;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class test {

    public static void main(String[] args) {
        // categoryID list: 1801, 316, 915, 1815, 1883, 44816, 49288, 49588, 53058
        Long categoryId = 1801l;
      //  createProductDTO(categoryId);
//        ReviewBLL reviewBLL = new ReviewBLL();
//        ReviewDTO review = reviewBLL.findById(561841l);
//        System.out.println(review.getContent());
        //createReviewDTO(categoryId);
        //createCommentDTO(categoryId);
        //createTimelineDTO(categoryId);
        createConfigurableProductDTO(categoryId);
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
                System.out.println("i=" +i);
                Connection.Response response = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute();
                Document document = response.parse();
                JsonNode node = new ObjectMapper().readTree(document.text()).get("data");
                for(JsonNode objNode1 : node)
                {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ReviewDTO reviewDto = new ObjectMapper().reader(ReviewDTO.class).readValue(objNode1);
                    reviewDto.getStatus();
                    ReviewBLL reviewBLL = new ReviewBLL();
                    reviewBLL.save(reviewDto);
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

    public static void createTimelineDTO(Long categoryId) {
        String ids = String.valueOf(categoryId);
        Get500ProductIds g = new Get500ProductIds();
        ArrayList list = new ArrayList();
        list = g.get500ProductIds(ids);
        for (int i = 0; i < list.size(); i++) {
            String url = "https://tiki.vn/api/v2/reviews?product_id=" + String.valueOf(list.get(i)) + "&include=comments&page=1&limit=-1";
            try {
                System.out.println("i=" +i);
                Connection.Response response = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute();
                Document document = response.parse();
                JsonNode node = new ObjectMapper().readTree(document.text()).get("data");
                for(JsonNode objNode1 : node)
                {
                    TimelineDTO timeline = new ObjectMapper().reader(TimelineDTO.class).readValue(objNode1);
                    TimelineBLL timelineBLL = new TimelineBLL();
                    timelineBLL.save(timeline);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void createConfigurableProductDTO(Long categoryId) {
        String ids = String.valueOf(categoryId);
        Get500ProductIds g = new Get500ProductIds();
        ArrayList list = new ArrayList();
        list = g.get500ProductIds(ids);
//        if(list.size() < 500)
//            System.out.println(list.size());
//        else {
            for (int i = 0; i < list.size(); i++) {
                String url = "https://tiki.vn/api/v2/products/" + String.valueOf(list.get(i));
                try {
                    System.out.println("i= " + i);
                    Long productId = (Long)list.get(i);
                    Connection.Response response = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute();
                    Document document = response.parse();
                    JsonNode node = new ObjectMapper().readTree(document.text()).get("configurable_products");
                    ConfigurableProductDTO config = new ObjectMapper()
                            .reader(ConfigurableProductDTO.class)
                            .readValue(node);
                    config.setProductId(productId);
                    ConfigurableProductBLL configurableProductBLL = new ConfigurableProductBLL();
                    configurableProductBLL.save(config);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        //}
    }

    public static void checkChangePrice()
    {
        
    }
}