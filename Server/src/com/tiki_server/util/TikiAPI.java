package com.tiki_server.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tiki_server.bll.*;
import com.tiki_server.bll.impl.*;
import com.tiki_server.dto.*;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TikiAPI {

    public TikiAPI() throws IOException {
    }

    public static void updateProducts() throws IOException {
        String url = "https://tiki.vn/api/v2/products/";

        IProductBLL productBLL = new ProductBLL();
        IHistoryBLL historyBLL = new HistoryBLL();

        List<ProductDTO> products = productBLL.findAll();

        if (products != null) {
            ObjectMapper mapper = new ObjectMapper();

            for (ProductDTO product : products) {
                String json = JSON.get(url + product.getId());
                System.out.println(product.getId());
                if (json == null || json.isEmpty())
                    System.out.println("Product ID: " + product.getId() + " has empty JSON: " + json);
                else {
                    ObjectNode rootNode = mapper.readValue(json, ObjectNode.class);

                    ProductDTO newProduct = mapper.readValue(json, ProductDTO.class);
                    String descriptionText = Jsoup.parse(newProduct.getDescription()).text();
                    newProduct.setDescription(descriptionText);

//                    Update Brand
                    TikiAPI.updateBrand(rootNode, newProduct);

//                    Update Category
                    TikiAPI.updateCategory(rootNode, newProduct);

//                    Update Configurable Product & its history
                    TikiAPI.updateConfigurableProduct(rootNode, newProduct);

//                    Update Product & its history
                    ProductDTO oldProduct = productBLL.findById(newProduct.getId());
                    if (!oldProduct.equals(newProduct)) {
                        productBLL.update(newProduct);

                        if (oldProduct.getPrice().compareTo(newProduct.getPrice()) != 0
                            || oldProduct.getListPrice().compareTo(newProduct.getListPrice()) != 0
                            || oldProduct.getOriginalPrice().compareTo(newProduct.getOriginalPrice()) != 0
                            || oldProduct.getDiscount().compareTo(newProduct.getDiscount()) != 0
                            || oldProduct.getDiscountRate() != newProduct.getDiscountRate()) {
                            HistoryDTO history = new HistoryDTO();
                            history.setDate(LocalDate.now());
                            history.setPrice(newProduct.getPrice());
                            history.setListPrice(newProduct.getListPrice());
                            history.setOriginalPrice(newProduct.getOriginalPrice());
                            history.setDiscount(newProduct.getDiscount());
                            history.setDiscountRate(newProduct.getDiscountRate());
                            history.setProductId(newProduct.getId());

                            historyBLL.save(history);
                        }
                    }
                }
            }
        }
    }

    public static void updateReviews() throws IOException, ParseException {
        String urlPrefix = "https://tiki.vn/api/v2/reviews?product_id=";
        String urlPostfix = "&include=comments&page=1&limit=-1";

        IProductBLL productBLL = new ProductBLL();

        List<ProductDTO> products = productBLL.findAll();

        if (products != null) {
            ObjectMapper mapper = new ObjectMapper();

            for (ProductDTO product : products) {
                String url = urlPrefix + product.getId() + urlPostfix;
                String json = JSON.get(url + product.getId());
                System.out.println(product.getId());
                if (json == null || json.isEmpty())
                    System.out.println("Product ID: " + product.getId() + " has empty JSON: " + json);
                else {
                    ObjectNode rootNode = mapper.readValue(json, ObjectNode.class);

//                    Update Review
                    updateReview(rootNode);
//                    Update Timeline
                    updateTimeline(rootNode);
//                    Update Comment
                    updateComment(rootNode);
                }
            }
        }
    }

    public static void updateBrand(ObjectNode rootNode, ProductDTO product) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        IBrandBLL brandBLL = new BrandBLL();

        if (brandBLL.findById(product.getBrandId()) == null) {
            JsonNode brandNode = rootNode.get("brand");
            if (brandNode != null) {
                BrandDTO newBrand = mapper.readValue(brandNode.toString(), BrandDTO.class);
                brandBLL.save(newBrand);
            }
        }
    }

    public static void updateCategory(ObjectNode rootNode, ProductDTO product) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ICategoryBLL categoryBLL = new CategoryBLL();

        if (categoryBLL.findById(product.getCategoryId()) == null) {
            JsonNode categoryNode = rootNode.get("categories");
            if (categoryNode != null) {
                CategoryDTO newCategory = mapper.readValue(categoryNode.toString(), CategoryDTO.class);
                categoryBLL.save(newCategory);
            }
        }
    }

    public static void updateConfigurableProduct(ObjectNode rootNode, ProductDTO product) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode cpNode = rootNode.get("configurable_products");

        IConfigurableProductBLL configurableProductBLL = new ConfigurableProductBLL();
        IConfigurableProductHistoryBLL cpHistoryBLL = new ConfigurableProductHistoryBLL();

        if (cpNode != null) {
            List<ConfigurableProductDTO> configurableProducts = mapper.readValue(cpNode.toString(),
                    mapper.getTypeFactory().constructCollectionType(List.class, ConfigurableProductDTO.class));

            for (ConfigurableProductDTO newcp : configurableProducts){
                newcp.setProductId(product.getId());

                ConfigurableProductDTO oldCP = configurableProductBLL.findByChildId(newcp.getChildId());

                if (oldCP != null) {
                    if (!oldCP.equals(newcp)) {
                        configurableProductBLL.update(newcp);
                        if (oldCP.getPrice().compareTo(newcp.getPrice()) != 0)
                            cpHistoryBLL.save(new ConfigurableProductHistoryDTO(LocalDate.now(), newcp.getPrice(), newcp.getChildId()));
                    }
                } else {
                    configurableProductBLL.save(newcp);
                    cpHistoryBLL.save(new ConfigurableProductHistoryDTO(LocalDate.now(), newcp.getPrice(), newcp.getChildId()));
                }
            }
        }
    }

    public static void updateReview(ObjectNode rootNode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ReviewBLL reviewBLL = new ReviewBLL();
        ProductBLL productBLL = new ProductBLL();
        JsonNode dataNode = rootNode.get("data");
        for(JsonNode dataArray : dataNode) {
            ReviewDTO review = objectMapper.treeToValue(dataArray, ReviewDTO.class);
            String regex = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
            Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
            Matcher matcher = pattern.matcher(review.getContent());
            String content = matcher.replaceAll("");
            content = content.replaceAll("\\xF0\\x9D\\x99\\xBA\\xE1\\xBA...","");
            review.setContent(content);

            if(reviewBLL.findById(review.getId()) == null) {
                if(productBLL.findById(review.getProductId()) != null) {
                    reviewBLL.save(review);
                    System.out.println("Lưu thành công review mới có review_id = " + review.getId());
                } else
                    System.out.println("Không tìm thấy product_id = " + review.getProductId() + " để lưu review");
            } else {
                ReviewDTO oldReview = reviewBLL.findById(review.getId());
                if(review.getCommentCount().compareTo(oldReview.getCommentCount()) != 0
                    || review.getStatus().compareTo(oldReview.getStatus()) !=  0
                    || (review.getImageUrl() == null ? "" : review.getImageUrl()).compareTo(oldReview.getImageUrl() == null ? "" : oldReview.getImageUrl()) != 0
                    || review.getRating() != oldReview.getRating()
                    || review.getTitle().compareTo(oldReview.getTitle()) != 0) {
                    reviewBLL.update(review);
                    System.out.println("Update thành công review_id = "+review.getId());
                }
            }
        }

    }

    public static void updateComment(ObjectNode rootNode) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ReviewBLL reviewBLL = new ReviewBLL();
        CommentBLL commentBLL = new CommentBLL();
        JsonNode data = rootNode.get("data");
        for(JsonNode dataArray : data) {
            JsonNode comments = dataArray.get("comments");
                for (JsonNode commentArray : comments) {
                    if (commentArray != null) {
                        CommentDTO comment = objectMapper.treeToValue(commentArray, CommentDTO.class);
                        String regex = "[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\s]";
                        Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
                        Matcher matcher = pattern.matcher(comment.getContent());
                        String content = matcher.replaceAll("");
                        comment.setContent(content);
                        if (reviewBLL.findById(comment.getReviewId()) == null)
                            System.out.println("Không tìm thấy review id = " + comment.getReviewId() + " để lưu comment_id = " + comment.getId());
                        else {
                            if (commentBLL.findById(comment.getId()) == null) {
                                commentBLL.save(comment);
                                System.out.println("Lưu thành công comment mới có comment_id = " + comment.getId());
                            } else {
                                CommentDTO oldComment = commentBLL.findById(comment.getId());
                                if ((comment.getAvatarUrl() == null ? "" : comment.getAvatarUrl()).compareTo(oldComment.getAvatarUrl() == null ? "" : oldComment.getAvatarUrl()) != 0
                                        || comment.getCommentator().compareTo(oldComment.getCommentator()) != 0
                                        || comment.getFullname().compareTo(oldComment.getFullname()) != 0
                                        || comment.getReported().compareTo(oldComment.getReported()) != 0
                                        || comment.getStatus() != oldComment.getStatus()) {
                                    commentBLL.update(comment);
                                    System.out.println("Update thành công comment mới có comment_id = " + comment.getId());
                                }
                            }
                        }
                    }
                }
        }
    }

    public static void updateTimeline(ObjectNode rootNode) throws IOException, ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ObjectMapper objectMapper = new ObjectMapper();
        ReviewBLL reviewBLL = new ReviewBLL();
        TimelineBLL timelineBLL = new TimelineBLL();
        JsonNode data = rootNode.get("data");
        for(JsonNode dataArray : data) {
            Long reviewId = dataArray.get("id").asLong();
            JsonNode timelines = dataArray.get("timeline");
            if(timelines != null) {
                TimelineDTO timeline = objectMapper.treeToValue(timelines, TimelineDTO.class);
                timeline.setReviewId(reviewId);
                String reviewCreatedDate = simpleDateFormat.format(timeline.getReviewCreatedDate());
                timeline.setReviewCreatedDate(simpleDateFormat.parse(reviewCreatedDate));
                String deliveryDate = simpleDateFormat.format(timeline.getDeliveryDate());
                timeline.setDeliveryDate(simpleDateFormat.parse(deliveryDate));
                if(reviewBLL.findById(timeline.getReviewId()) == null)
                    System.out.println("Không tìm thấy review_id = "+timeline.getReviewId()+" để lưu timeline");
                else {
                    TimelineDTO oldTimeline = timelineBLL.findByReviewId(reviewId);
                    if(oldTimeline == null) {
                        timelineBLL.save(timeline);
                        System.out.println("Lưu thành công timeline mới có review_id = " + timeline.getReviewId());
                    } else if(timeline.getReviewCreatedDate().compareTo(oldTimeline.getReviewCreatedDate()) != 0
                    || timeline.getDeliveryDate().compareTo(oldTimeline.getDeliveryDate()) != 0
                    || timeline.getContent().compareTo(oldTimeline.getContent()) != 0
                    || timeline.getExplaination().compareTo(oldTimeline.getExplaination()) != 0) {
                        timelineBLL.updateByReviewId(timeline);
                        System.out.println("Update thành công timeline mới có review_id = " + timeline.getReviewId());
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            updateProducts();
            updateReviews();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}


