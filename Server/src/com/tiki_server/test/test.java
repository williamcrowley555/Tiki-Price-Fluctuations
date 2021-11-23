package com.tiki_server.test;

import com.tiki_server.bll.impl.ProductBLL;
import com.tiki_server.dto.ProductDTO;
import com.tiki_server.util.Get500ProductIds;
import com.tiki_server.util.GetProductDetails;

import java.util.ArrayList;
import java.util.HashMap;

public class test {
    public static void main(String[] args) {
        // categoryId = 1883,
//        Long categoryId = 1883l;
//        createProductDTO(categoryId);

        String categoryId = "1883";
//        Get500ProductIds array = new Get500ProductIds();
//        ArrayList<Long> ids = array.get500ProductIds(categoryId);
//
//        for(int i=0;i<ids.size();i++) {
//            GetProductDetails a = new GetProductDetails();
//            a.getProductDetails(Long.parseLong(String.valueOf(ids.get(i))));
//        }
        createProductDTO(categoryId);
    }

    public static void createProductDTO(String categoryId) {
        String ids = String.valueOf(categoryId);
        Get500ProductIds g = new Get500ProductIds();
        ArrayList list = new ArrayList();
        list = g.get500ProductIds(ids);
        for (int i = 0; i < list.size(); i++){
            GetProductDetails gp = new GetProductDetails();
            HashMap map = gp.getProductDetails(Long.parseLong(String.valueOf(list.get(i))));
            ProductDTO product = new ProductDTO();
            product.setId(Long.parseLong(String.valueOf(map.get("id"))));
            product.setSku(String.valueOf(map.get("sku")));
            product.setName(String.valueOf(map.get("name")));
            product.setUrlKey(String.valueOf(map.get("urlKey")));
            product.setUrlPath(String.valueOf(map.get("urlPath")));
            product.setPrice(Long.parseLong(String.valueOf(map.get("price"))));
            product.setListPrice(Long.parseLong(String.valueOf(map.get("listPrice"))));
            product.setOriginalPrice(Long.parseLong(String.valueOf(map.get("originalPrice"))));
            product.setDiscount(Long.parseLong(String.valueOf(map.get("discount"))));
            product.setDiscountRate(Long.parseLong(String.valueOf(map.get("discountRate"))));
            product.setRatingAverage(Float.parseFloat(String.valueOf(map.get("ratingAverage"))));
            product.setReviewCount(Long.parseLong(String.valueOf(map.get("reviewCount"))));
            product.setFavoriteCount(Long.parseLong(String.valueOf(map.get("favoriteCount"))));
            product.setShortDescription(String.valueOf(map.get("shortDescription")));
            product.setDescription(String.valueOf(map.get("description")));
            product.setImageUrl(String.valueOf(map.get("imageUrl")));
            if(map.get("allTimeQuantity") == null)
                product.setAllTimeQuantitySold(0l);
            else
                product.setAllTimeQuantitySold(Long.parseLong(String.valueOf(map.get("allTimeQuantitySold"))));
            product.setCategoryId(Long.parseLong(categoryId));
            ProductBLL productBLL = new ProductBLL();
            productBLL.save(product);
        }

    }
}