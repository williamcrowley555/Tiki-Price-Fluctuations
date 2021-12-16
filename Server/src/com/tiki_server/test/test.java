package com.tiki_server.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tiki_server.bll.IProductBLL;
import com.tiki_server.bll.impl.*;
import com.tiki_server.dal.IProductDAL;
import com.tiki_server.dal.impl.ProductDAL;
import com.tiki_server.dto.*;
import com.tiki_server.action.Get500ProductIds;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    public static void main(String[] args) throws IOException {
        List<ProductDTO> listProductAdvance = new ArrayList<>();
        IProductBLL productBLL = new ProductBLL();
        String productName = "a";
        String brandName = "C";
        Long categoryId = 1815l;
        System.out.println(categoryId);
        double ratingAverage = 5;
        Long minPrice = 1000l;
        Long maxPrice = 1000000000l;

        //listProductAdvance = productBLL.findAdvance(productName,brandName,categoryId,ratingAverage,minPrice,maxPrice);
        System.out.println(listProductAdvance.size());

        // categoryID list: 316, 915, 1815, 1801, 1883, 44816, 49288, 49588, 53058
//        long[] categoryIdList = new long[]{316l, 915l, 1815l, 1801l, 1883l, 44816l, 49288l, 49588l, 53058l};
//        for(int i=0;i<9;i++) {
//            createProductDTO(categoryIdList[i]);
//            createReviewDTO(categoryIdList[i]);
//            createCommentDTO(categoryIdList[i];
//            createTimelineDatabase(categoryIdList[i]);
//           createConfigurableProductDatabase(categoryIdList[i]);
//        }
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
                        CommentDTO commentDto = new ObjectMapper().readValue(objNode2.toString(), CommentDTO.class);
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

    public static void createTimelineDatabase(Long categoryId) throws IOException {
        ArrayList<Long> productIdList = getProductIdListFromCategoryId(categoryId);
        ObjectMapper objectMapper = new ObjectMapper();
        ReviewBLL reviewBLL = new ReviewBLL();
        ReviewDTO existReview;
        TimelineBLL timelineBLL = new TimelineBLL();
        TimelineDTO timelineDTO;

        if(productIdList == null || productIdList.size()==0) {
            System.out.println("Không tìm được danh sách product_id của category_id = "+categoryId);
            return;
        }

        for(Object productId : productIdList)
        {
            Long id = (Long) productId;
            String json = getJsonFromTikiAPI("timeline",id);
            ObjectNode rootNode = objectMapper.readValue(json,ObjectNode.class);
            if(rootNode == null)
                System.out.println("Không tìm được timeline của product_id = "+categoryId);
            else {
                JsonNode dataNode = rootNode.get("data");
                for(JsonNode dataArray : dataNode) {
                    Long reviewId = dataArray.get("id").asLong();
                    //System.out.println(reviewId);
                    existReview = reviewBLL.findById(reviewId);
                    if(existReview == null)
                        System.out.println("Không tìm thấy review_id = "+reviewId+" để lưu timeline");
                    else {
                        JsonNode timeline = dataArray.get("timeline");
                        if (timeline != null) {
                            timelineDTO = objectMapper.reader(TimelineDTO.class).readValue(timeline);
                            timelineDTO.setReviewId(reviewId);
                            timelineBLL.save(timelineDTO);
                            System.out.println("Lưu thành công timeline có review_id = "+reviewId);
                        }
                    }
                }
            }
        }
    }

    public static void createConfigurableProductDatabase(Long categoryId) throws IOException {
        ArrayList<Long> productIdList = getProductIdListFromCategoryId(categoryId);
        ObjectMapper objectMapper = new ObjectMapper();
        ConfigurableProductBLL configurableProductBLL = new ConfigurableProductBLL();
        ConfigurableProductDTO existConfigurableProduct;
        ConfigurableProductDTO configurableProduct;
        ProductBLL productBLL = new ProductBLL();
        ProductDTO existProduct;

        if(productIdList == null || productIdList.size()==0) {
            System.out.println("Không tìm được danh sách product_id của category_id = "+categoryId);
            return;
        }
        for(Object productId : productIdList) {
            Long id = (Long) productId;
            String json = getJsonFromTikiAPI("configurableProduct", id);
            ObjectNode rootNode = objectMapper.readValue(json, ObjectNode.class);
            if (rootNode == null)
                System.out.println("Không tìm được timeline của product_id = " + categoryId);
            else {
                JsonNode configurableProductNode = rootNode.get("configurable_products");
                if(configurableProductNode != null)
                    for (JsonNode configurableProductArray : configurableProductNode) {
                        if(configurableProductArray != null) {
                            configurableProduct = objectMapper.readValue(configurableProductArray.asText(), ConfigurableProductDTO.class);
                            existProduct = productBLL.findById(configurableProduct.getChildId());
                            if (existProduct == null)
                                System.out.println("Không tồn tại product_id = " + configurableProduct.getChildId() + " để lưu Configurable Product");
                            else {
                                existConfigurableProduct = configurableProductBLL.findByChildId(configurableProduct.getChildId());
                                if (existConfigurableProduct != null)
                                    System.out.println("Configurable Product với id = " + configurableProduct.getChildId() + "đã tồn tại");
                                else {
                                    configurableProductBLL.save(configurableProduct);
                                    System.out.println("Lưu configurableProduct_id = " + configurableProduct + " thành công");
                                }
                            }
                        }
                    }
            }
        }
    }

    private static ArrayList<Long> getProductIdListFromCategoryId(Long categoryId) throws IOException {
        ArrayList<Long> productIdList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        String json = getJsonFromTikiAPI("category",categoryId);

        if(json == null)
            return null;

        ObjectNode rootNode = objectMapper.readValue(json,ObjectNode.class);
        JsonNode dataNode = rootNode.get("data");
        if(dataNode == null)
            return null;

        for(JsonNode arrayData : dataNode)
            productIdList.add(arrayData.get("id").asLong());
        if(productIdList.size()==0)
            return null;

        return productIdList;
    }

    private static String getJsonFromTikiAPI(String selectedId,Long id) {
        String json = null;
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = null;

        String urlCategory = "https://tiki.vn/api/personalish/v1/blocks/listings?limit=100&categoryId="
                + id + "&category=" + id;
        String urlProduct = "https://tiki.vn/api/v2/products/"+ id;
        String urlReview = "https://tiki.vn/api/v2/reviews?product_id=" + id + "&include=comments&page=1&limit=-1";

        try {
            if (selectedId.equals("product") || selectedId.equals("configurableProduct"))
                json = Jsoup.connect(urlProduct).ignoreContentType(true).execute().body();
            else if (selectedId.equals("timeline") || selectedId.equals("review") || selectedId.equals("comment"))
                json = Jsoup.connect(urlReview).ignoreContentType(true).execute().body();
            else if (selectedId.equals("category"))
                json = Jsoup.connect(urlCategory).ignoreContentType(true).execute().body();
            return json;
        } catch (IOException ioe) {
            return null;
        }
    }
}