package com.tiki_server.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tiki_server.bll.*;
import com.tiki_server.bll.impl.*;
import com.tiki_server.dto.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TikiAPI {
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
                    System.out.println("Empty JSON: " + json);
                else {
                    ObjectNode rootNode = mapper.readValue(json, ObjectNode.class);

                    ProductDTO newProduct = mapper.readValue(json, ProductDTO.class);

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

    public static void main(String[] args) {
        try {
            updateProducts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


