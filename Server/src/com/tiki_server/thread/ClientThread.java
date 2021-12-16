package com.tiki_server.thread;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tiki_server.bll.*;
import com.tiki_server.bll.impl.*;
import com.tiki_server.dto.*;
import com.tiki_server.enums.MessageType;
import com.tiki_server.model.Message;
import com.tiki_server.util.AESUtil;
import com.tiki_server.util.BytesUtil;
import com.tiki_server.util.RSAUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.util.*;

public class ClientThread implements Runnable {
    private boolean isRunning = true;
    private Socket clientSocket;

    private ObjectOutput out;
    private ObjectInput in;

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private SecretKey secretKey;

    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        String json;
        Message clientRequest;

        try {
            this.in = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            this.out = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

            while (isRunning) {
                json = (String) in.readObject();
                clientRequest = new ObjectMapper().readValue(json, Message.class);

                if (clientRequest != null) {
                    String encryptedRequestContent = (String) clientRequest.getContent();
                    Map<String, Object> requestContent;

                    Message response = null;
                    Map<String, Object> responseContent = null;

                    IProductBLL productBLL = null;
                    IConfigurableProductBLL cpBLL = null;
                    IHistoryBLL historyBLL = null;
                    IConfigurableProductHistoryBLL cpHistoryBLL = null;
                    IReviewBLL reviewBLL = null;
                    ITimelineBLL timeLineBLL = null;
                    ICategoryBLL categoryBLL = null;
                    IBrandBLL brandBLL = null;

                    switch (clientRequest.getMessageType()) {
                        case GET_PUBLIC_KEY:
                            sendPublicKey();
                            break;

                        case SEND_SECRET_KEY:
                            byte[] msgContentInBytes = RSAUtil.decrypt(privateKey, Base64.getDecoder().decode(encryptedRequestContent));
                            String msgContentInJSON = (String) BytesUtil.encode(msgContentInBytes);
                            requestContent = new ObjectMapper().readValue(msgContentInJSON, Map.class);

                            String strSecretKey = (String) requestContent.get("strSecretKey");
                            secretKey = AESUtil.getAESKey(Base64.getDecoder().decode(strSecretKey));
                            break;

                        case GET_PRODUCT:
                            requestContent = (Map<String, Object>) decryptContent(secretKey, Base64.getDecoder().decode(encryptedRequestContent));
                            Long productId = Long.valueOf((int) requestContent.get("productId"));

                            productBLL = new ProductBLL();
                            ProductDTO product = productBLL.findById(productId);

                            responseContent = new HashMap<>();
                            responseContent.put("product", product);

                            response = new Message(responseContent, MessageType.PRODUCT_INFO);
                            sendMessage(response);
                            break;

                        case FILTER_PRODUCTS:
                            requestContent = (Map<String, Object>) decryptContent(secretKey, Base64.getDecoder().decode(encryptedRequestContent));

                            if (requestContent == null) {
                                response = createErrorMessage("Content can not be null");
                                sendMessage(response);
                            } else {
                                String productName = requestContent.containsKey("productName") ? (String) requestContent.get("productName") : null;
                                Long categoryId = requestContent.containsKey("categoryId") ? Long.valueOf((int) requestContent.get("categoryId")) : null;
                                Long brandId = requestContent.containsKey("brandId") ? Long.valueOf((int) requestContent.get("brandId")) : null;
                                Float ratingAverage = requestContent.containsKey("ratingAverage") ? ((Double) requestContent.get("ratingAverage")).floatValue() : null;
                                Long minPrice = requestContent.containsKey("minPrice") ? Long.valueOf((int) requestContent.get("minPrice")) : null;
                                Long maxPrice = requestContent.containsKey("maxPrice") ? Long.valueOf((int) requestContent.get("maxPrice")) : null;

                                productBLL = new ProductBLL();
                                List<ProductDTO> products = productBLL.filter(productName, categoryId, brandId, ratingAverage, minPrice, maxPrice);

                                responseContent = new HashMap<>();

                                if (products != null && products.isEmpty())
                                    products = null;

                                responseContent.put("products", products);
                                response = new Message(responseContent, MessageType.PRODUCTS);
                                sendMessage(response);
                            }
                            break;

                        case GET_CONFIGURABLE_PRODUCTS:
                            requestContent = (Map<String, Object>) decryptContent(secretKey, Base64.getDecoder().decode(encryptedRequestContent));

                            productId = Long.valueOf((int) requestContent.get("productId"));

                            cpBLL = new ConfigurableProductBLL();
                            List<ConfigurableProductDTO> configurableProducts = cpBLL.findByProductId(productId);

                            responseContent = new HashMap<>();

                            if (configurableProducts != null && configurableProducts.isEmpty())
                                configurableProducts = null;

                            responseContent.put("configurableProducts", configurableProducts);
                            response = new Message(responseContent, MessageType.CONFIGURABLE_PRODUCTS);
                            sendMessage(response);
                            break;

                        case GET_PRODUCT_HISTORIES_BY_URL:
                            requestContent = (Map<String, Object>) decryptContent(secretKey, Base64.getDecoder().decode(encryptedRequestContent));

                            String url = (String) requestContent.get("url");
                            int month = (int) requestContent.get("month");
                            int year = (int) requestContent.get("year");

                            historyBLL = new HistoryBLL();
                            List<HistoryDTO> histories = historyBLL.findByProductPageUrl(url, month, year);

                            responseContent = new HashMap<>();

                            if (histories != null && histories.isEmpty())
                                histories = null;

                            responseContent.put("productHistories", histories);
                            response = new Message(responseContent, MessageType.PRODUCT_HISTORIES);
                            sendMessage(response);
                            break;

                        case GET_PRODUCT_HISTORIES_BY_PRODUCT_ID:
                            requestContent = (Map<String, Object>) decryptContent(secretKey, Base64.getDecoder().decode(encryptedRequestContent));

                            System.out.println("hello");
                            productId = Long.valueOf((int) requestContent.get("productId"));
                            month = (int) requestContent.get("month");
                            year = (int) requestContent.get("year");

                            historyBLL = new HistoryBLL();
                            histories = historyBLL.findByProductId(productId, month, year);

                            responseContent = new HashMap<>();

                            if (histories != null && histories.isEmpty()) {
                                histories = null;
                            }

                            responseContent.put("productHistories", histories);
                            response = new Message(responseContent, MessageType.PRODUCT_HISTORIES_BY_ID);
                            sendMessage(response);
                            break;

                        case GET_CONFIGURABLE_PRODUCT_HISTORIES:
                            requestContent = (Map<String, Object>) decryptContent(secretKey, Base64.getDecoder().decode(encryptedRequestContent));

                            productId = Long.valueOf((int) requestContent.get("productId"));
                            String option1 = requestContent.get("option1") == null ? null : requestContent.get("option1").toString();
                            String option2 = requestContent.get("option2") == null ? null : requestContent.get("option2").toString();
                            String option3 = requestContent.get("option3") == null ? null : requestContent.get("option3").toString();
                            month = (int) requestContent.get("month");
                            year = (int) requestContent.get("year");

                            cpHistoryBLL = new ConfigurableProductHistoryBLL();
                            List<ConfigurableProductHistoryDTO> cpHistories = cpHistoryBLL.findByProductIdAndConfigurableOptions(productId, option1, option2, option3, month, year);

                            responseContent = new HashMap<>();

                            if (cpHistories != null && cpHistories.isEmpty())
                                cpHistories = null;

                            responseContent.put("configurableProductHistories", cpHistories);
                            response = new Message(responseContent, MessageType.CONFIGURABLE_PRODUCT_HISTORIES);
                            sendMessage(response);
                            break;

                        case GET_REVIEWS_BY_PRODUCT_ID:
                            requestContent = (Map<String, Object>) decryptContent(secretKey, Base64.getDecoder().decode(encryptedRequestContent));

                            productId = Long.valueOf((int) requestContent.get("productId"));

                            reviewBLL = new ReviewBLL();
                            List<ReviewDTO> reviews = reviewBLL.findByProductId(productId);

                            responseContent = new HashMap<>();

                            if (reviews != null && reviews.isEmpty())
                                reviews = null;

                            responseContent.put("reviews", reviews);
                            response = new Message(responseContent, MessageType.REVIEWS);
                            sendMessage(response);
                            break;

                        case GET_TIMELINE_BY_REVIEWID:
                            requestContent = (Map<String, Object>) decryptContent(secretKey, Base64.getDecoder().decode(encryptedRequestContent));

                            Long reviewId = Long.valueOf((int) requestContent.get("reviewId"));

                            timeLineBLL = new TimelineBLL();
                            TimelineDTO timeline = timeLineBLL.findByReviewId(reviewId);
                            responseContent = new HashMap<>();
                            responseContent.put("timeline", timeline);
                            response = new Message(responseContent, MessageType.TIMELINE_BY_REVIEWID);
                            sendMessage(response);
                            break;

                        case GET_CATEGORIES:
                            requestContent = (Map<String, Object>) decryptContent(secretKey, Base64.getDecoder().decode(encryptedRequestContent));

                            categoryBLL = new CategoryBLL();
                            List<CategoryDTO> categories = categoryBLL.findAll();

                            responseContent = new HashMap<>();

                            if (categories != null && categories.isEmpty())
                                categories = null;

                            responseContent.put("categories", categories);
                            response = new Message(responseContent, MessageType.CATEGORIES);
                            sendMessage(response);
                            break;

                        case GET_BRANDS_BY_CATEGORY_ID:
                            requestContent = (Map<String, Object>) decryptContent(secretKey, Base64.getDecoder().decode(encryptedRequestContent));
                            Long categoryId = Long.valueOf((int) requestContent.get("categoryId"));
                            brandBLL = new BrandBLL();
                            List<BrandDTO> brands = brandBLL.findByCategoryId(categoryId);

                            responseContent = new HashMap<>();

                            if (brands != null && brands.isEmpty())
                                brands = null;

                            responseContent.put("brands", brands);
                            response = new Message(responseContent, MessageType.BRANDS_BY_CATEGORY_ID);
                            sendMessage(response);
                            break;

                        case GET_ADVANCE_PRODUCTS:
                            productBLL = new ProductBLL();
                            requestContent = (Map<String, Object>) decryptContent(secretKey, Base64.getDecoder().decode(encryptedRequestContent));

                            System.out.println(requestContent);

                            String categoryName = requestContent.containsKey("category") ? (String) requestContent.get("productName") : null;
                            String productName = requestContent.containsKey("productName") ? (String) requestContent.get("productName") : null;
                            Double ratingAverage = requestContent.containsKey("ratingAverage") ? Double.parseDouble((String) requestContent.get("ratingAverage")) : null;
                            Long minPrice = requestContent.containsKey("fromMoney") ? Long.valueOf((String) requestContent.get("fromMoney")) : null;
                            Long maxPrice = requestContent.containsKey("toMoney") ? Long.valueOf((String) requestContent.get("toMoney")) : null;
                            String listBrands = (String) requestContent.get("brands");

                            StringTokenizer tokenizer = new StringTokenizer(listBrands, "-");
                            List<String> brandNames = new ArrayList<>();
                            while (tokenizer.hasMoreTokens())
                                brandNames.add(tokenizer.nextToken());

                            List<List<ProductDTO>> listAdvanceProduct = new ArrayList<>();
                            if(brandNames.size() == 0)
                            {
                                String brand = null;
                                List<ProductDTO> productAdvance = productBLL.findAdvance(productName, brand, categoryName, ratingAverage, minPrice, maxPrice);
                            } else {
                                for (String brand : brandNames) {
                                    brand = brand.toUpperCase();
                                    List<ProductDTO> productAdvance = productBLL.findAdvance(productName, brand, categoryName, ratingAverage, minPrice, maxPrice);

                                    if (productAdvance != null)
                                        listAdvanceProduct.add(productAdvance);
                                }
                            }

                            responseContent = new HashMap<>();
                            responseContent.put("list_advance_products", listAdvanceProduct);
                            response = new Message(responseContent, MessageType.ADVANCE_PRODUCTS);
                            sendMessage(response);
                            break;

                        case USER_DISCONNECT:
                            isRunning = false;
                            response = new Message(null, MessageType.USER_DISCONNECT);
                            sendMessage(response);
                            break;

                        default:
                            response = createErrorMessage("Invalid Message!");
                            sendMessage(response);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void sendPublicKey() throws IOException, NoSuchAlgorithmException {
//        Generate Key pair to get Public & Private key
        KeyPair keyPair = RSAUtil.generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
        String strPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());

//        Send Public key to Client
        Map<String, Object> msgContent = new HashMap<>();
        msgContent.put("strPublicKey", strPublicKey);

        Message msg = new Message(msgContent, MessageType.PUBLIC_KEY);
        sendMessage(msg);
    }

    private void sendMessage(Message message) throws IOException {
        String msgContentInJSON = new ObjectMapper().writeValueAsString(message.getContent());
        byte[] msgContentInBytes = BytesUtil.decode(msgContentInJSON);

        if (message.getMessageType().equals(MessageType.PUBLIC_KEY)) {
            String content = Base64.getEncoder().encodeToString(msgContentInBytes);

            message.setContent(content);
        } else {
            try {
                if (this.secretKey != null) {
                    byte[] encryptedMsgContent = AESUtil.encrypt(this.secretKey, msgContentInBytes);
                    String content = Base64.getEncoder().encodeToString(encryptedMsgContent);

                    message.setContent(content);
                } else {
                    System.err.println("Error: No Secret Key Found");
                    return;
                }
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
        }

        String json = new ObjectMapper().writeValueAsString(message);

        out.writeObject(json);
        out.flush();
    }

    public Object decryptContent(SecretKey secretKey, byte[] content) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IOException {
        byte[] ivBytes = Arrays.copyOfRange(content, 0, 16);
        byte[] contentInBytes = Arrays.copyOfRange(content, 16, content.length);

        IvParameterSpec ivParams = AESUtil.getIVParams(ivBytes);

        byte[] decryptedContent = AESUtil.decrypt(secretKey, ivParams, contentInBytes);
        String contentInJSON = (String) BytesUtil.encode(decryptedContent);

        return new ObjectMapper().readValue(contentInJSON, Map.class);
    }

    private Message createErrorMessage(String content) {
        Map<String, Object> msgContent = new HashMap<>();
        msgContent.put("error", content);

        return new Message(msgContent, MessageType.ERROR);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
}
