package com.tiki_server.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiki_server.bll.impl.*;
import com.tiki_server.dto.*;
import com.tiki_server.action.Get500ProductIds;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    public static void main(String[] args) {
        Long categoryId = 316l;
        // categoryID list: 1801, 316, 915, 1815, 1883, 44816, 49288, 49588, 53058
        ArrayList<Long> categoryIdList = new ArrayList();
        categoryIdList.add(316l);
        categoryIdList.add(915l);
        categoryIdList.add(1801l);
        categoryIdList.add(1815l);
        categoryIdList.add(1883l);
        categoryIdList.add(44816l);
        categoryIdList.add(49288l);
        categoryIdList.add(49588l);
        categoryIdList.add(53058l);
        for(int i=0;i<categoryIdList.size();i++) {
            //  createProductDTO(categoryId);
//            createReviewDTO(categoryId);
//            createCommentDTO(categoryId);
            //createTimelineDTO(categoryId);
            //createConfigurableProductDTO(categoryIdList.get(i));
        }
    }

    public static void createProductDTO(Long categoryId) {
        String ids = String.valueOf(categoryId);
        Get500ProductIds g = new Get500ProductIds();
        ArrayList list = new ArrayList();
        list = g.get500ProductIds(ids);
        System.out.println(list.get(316));

        for (int i = 0; i < list.size(); i++) {
            String url = "https://tiki.vn/api/v2/products/" + String.valueOf(list.get(i));
            try {
                System.out.println("i= "+i);
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
                ProductDTO existProduct = productBLL.findById(product.getId());
                if(existProduct!=null)
                {
                    System.out.println("Product id: "+product.getId()+" đã tồn tại");
                }
                else{
                    productBLL.save(product);
                    System.out.println("Lưu sản phẩm với id = "+product.getId());
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void createReviewDTO(Long categoryId) {
        String ids = String.valueOf(categoryId);
        Get500ProductIds g = new Get500ProductIds();
        ArrayList list = new ArrayList();
        list = g.get500ProductIds(ids);
//        System.out.println(list.get(316));
        for (int i = 0; i < list.size(); i++) {
            String url = "https://tiki.vn/api/v2/reviews?product_id=" + String.valueOf(list.get(i)) + "&include=comments&page=1&limit=-1";
            try {
                System.out.println("i=" +i);
                Connection.Response response = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute();
                Document document = response.parse();
                JsonNode node = new ObjectMapper().readTree(document.text()).get("data");
                ProductBLL productBLL = new ProductBLL();
                for(JsonNode objNode1 : node)
                {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ReviewDTO reviewDto = new ObjectMapper().reader(ReviewDTO.class).readValue(objNode1);
                    String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
                    Pattern pattern = Pattern.compile(
                            regex,
                            Pattern.UNICODE_CHARACTER_CLASS);
                    Matcher matcher = pattern.matcher(reviewDto.getContent());
                    String content = matcher.replaceAll("");
                    reviewDto.setContent(content);
                    matcher = pattern.matcher(reviewDto.getTitle());
                    String title = matcher.replaceAll("");
                    reviewDto.setTitle(title);
                    ReviewBLL reviewBLL = new ReviewBLL();
                    ReviewDTO existReview = reviewBLL.findById(reviewDto.getId());
                    if(existReview != null)
                    {
                        System.out.println("Review với id = "+reviewDto.getId() + " đã tồn tại");
                    }
                    else
                    {
                        ProductDTO existProduct = productBLL.findById(reviewDto.getProductId());
                        if(existProduct != null)
                        {
                            reviewBLL.save(reviewDto);
                            System.out.println("Lưu thành công review có id = "+reviewDto.getId());
                        }
                        else
                        {
                            System.out.println("Không tìm thấy Product id: "+reviewDto.getProductId()+" để lưu review");
                        }
                    }
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
                System.out.println("i= "+i);
                Connection.Response response = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute();
                Document document = response.parse();
                JsonNode node = new ObjectMapper().readTree(document.text()).get("data");
                for(JsonNode objNode1 : node)
                {
                    JsonNode comments =  objNode1.get("comments");
                    for(JsonNode objNode2 : comments)
                    {
                        CommentDTO commentDto = new ObjectMapper().reader(CommentDTO.class).readValue(objNode2);
                        String regex = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
                        Pattern pattern = Pattern.compile(
                                regex,
                                Pattern.UNICODE_CHARACTER_CLASS);
                        Matcher matcher = pattern.matcher(commentDto.getContent());
                        String content = matcher.replaceAll("");
                        commentDto.setContent(content);
                        ReviewBLL reviewBLL = new ReviewBLL();
                        CommentBLL commentBLL = new CommentBLL();
                        ReviewDTO existReview = reviewBLL.findById(commentDto.getReviewId());
                        if(existReview == null)
                        {
                            System.out.println("Không tìm thấy review_id = "+commentDto.getReviewId()+" để lưu comment");
                        }
                        else
                        {
                            CommentDTO existComment = commentBLL.findById(commentDto.getId());
                            if(existComment != null)
                            {
                                System.out.println("Comment với id = "+commentDto.getId()+" đã tồn tại");
                            }
                            else {
                                commentBLL.save(commentDto);
                                System.out.println("Lưu thành công comment với id = "+commentDto.getId());
                            }
                        }
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
                    Connection.Response response = Jsoup.connect(url).ignoreContentType(true).method(Connection.Method.GET).execute();
                    Document document = response.parse();
                    JsonNode node = new ObjectMapper().readTree(document.text()).get("configurable_products");
                    if(node!= null) {
                        for (JsonNode objNode : node) {
                            ConfigurableProductDTO config = new ObjectMapper()
                                    .reader(ConfigurableProductDTO.class)
                                    .readValue(objNode);
                            config.setProductId(Long.parseLong(String.valueOf(list.get(i))));
                            ConfigurableProductBLL configurableProductBLL = new ConfigurableProductBLL();
                            ProductBLL productBLL = new ProductBLL();
                            ConfigurableProductDTO existConfig = configurableProductBLL.findById(config.getId());
                            if(existConfig != null)
                                System.out.println("Configurable Product với id = "+config.getProductId()+" đã tồn tại");
                            else {
                                ProductDTO exitsProduct = productBLL.findById(config.getProductId());
                                if(exitsProduct!=null) {
                                    configurableProductBLL.save(config);
                                    System.out.println("Lưu thành công Configurable Product với id = " + config.getId());
                                } else {
                                    System.out.println("Không tìm thấy Product với id = " + config.getProductId() + " để lưu Configurable Product");
                                }
                            }
                        }
                    }
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