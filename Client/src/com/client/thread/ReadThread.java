package com.client.thread;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.client.enums.MessageType;
import com.client.gui.panelTimNangCao;
import com.client.main.Client;
import com.client.model.Message;
import com.client.util.AESUtil;
import com.client.util.BytesUtil;
import com.client.util.RSAUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.*;

public class ReadThread implements Runnable {
    private volatile boolean isRunning = true;

    private Client client;
    private Socket socket;

    private ByteArrayInputStream byteInputStream = null;
    private ObjectInput in = null;
    private String productName;
    private List<LinkedHashMap<String, Object>> timelines;
    public panelTimNangCao panelAdvance = new panelTimNangCao(client);

    public ReadThread(Client client, Socket socket) throws IOException {
        this.socket = socket;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            this.in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            String json;
            Message response;
                
            while(isRunning) {
//            Get response from server
                json = (String) in.readObject();
                response = new ObjectMapper().readValue(json, Message.class);

                if (response != null) {
                    String encryptedContent = (String) response.getContent();
                    Map<String, Object> responseContent;

                    switch (response.getMessageType()) {
                        case PUBLIC_KEY:
                            String responseContentInJSON = (String) BytesUtil.encode(Base64.getDecoder().decode(encryptedContent));
                            responseContent = new ObjectMapper().readValue(responseContentInJSON, Map.class);
                            String strPublicKey = (String) responseContent.get("strPublicKey");
                            PublicKey publicKey = RSAUtil.getPublicKey(Base64.getDecoder().decode(strPublicKey));
                            this.client.setPublicKey(publicKey);

//                            Generate AES key
                            SecretKey secretKey = AESUtil.generateAESKey();
                            this.client.setSecretKey(secretKey);
                            String strSecretKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

                            Map<String, Object> msgContent = new HashMap<>();
                            msgContent.put("strSecretKey", strSecretKey);

                            this.client.sendMessage(new Message(msgContent, MessageType.SEND_SECRET_KEY));
                            break;

                        case PRODUCT_INFO:
                            responseContent = (Map<String, Object>) decryptContent(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));

                            //System.out.println(responseContent.get("product"));
                            LinkedHashMap<String, Object> recvProduct;
                            recvProduct = (LinkedHashMap<String, Object>) responseContent.get("product");
                            client.updateProductInfoURL((String) recvProduct.get("name"), (String) recvProduct.get("imageUrl") );
                            this.productName = (String) recvProduct.get("name");
                            System.out.println("Client receive: " + recvProduct);
                            client.setCurrentProduct(recvProduct);
                            client.getReviews(Long.valueOf((int)recvProduct.get("id")));
                            break;

                        case PRODUCTS:
                            responseContent = (Map<String, Object>) decryptContent(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));
                            
                            List<LinkedHashMap<String, Object>> recvProducts = (List<LinkedHashMap<String, Object>>) responseContent.get("products");
                            System.out.println("Client receive: ");
                            recvProducts.forEach(System.out::println);
                            break;

                        case CONFIGURABLE_PRODUCTS:
                            responseContent = (Map<String, Object>) decryptContent(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));
                            List<LinkedHashMap<String, Object>> recvCPs = (List<LinkedHashMap<String, Object>>) responseContent.get("configurableProducts");
                            System.out.println("Client receive: ");
                            recvCPs.forEach(System.out::println);
                            break;

                        case PRODUCT_HISTORIES:
                           

                            responseContent = (Map<String, Object>) decryptContent(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));
                            List<LinkedHashMap<String, Object>> recvProductHistories = (List<LinkedHashMap<String, Object>>) responseContent.get("productHistories");
                           
                            //recvProductHistories.forEach(System.out::println);
                            
                            System.out.println("Client receive: ");
                            
                            if (recvProductHistories != null)
                            client.getProduct(Long.valueOf((int) recvProductHistories.get(0).get("productId")));
                            client.updateLineChartURL(recvProductHistories, "");
                            break;
                            
                        case PRODUCT_HISTORIES_BY_ID:
                            responseContent = (Map<String, Object>) decryptContent(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));
                            List<LinkedHashMap<String, Object>> recvProductHistoriesById = (List<LinkedHashMap<String, Object>>) responseContent.get("productHistories");
                            
                            System.out.println("Client receive: ");
                            
                            client.updateLineChartAdvance(recvProductHistoriesById, "");
                            break;

                        case CONFIGURABLE_PRODUCT_HISTORIES:
                            responseContent = (Map<String, Object>) decryptContent(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));

                            List<LinkedHashMap<String, Object>> recvCPHistories = (List<LinkedHashMap<String, Object>>) responseContent.get("configurableProductHistories");
                            System.out.println("Client receive: ");
                            recvCPHistories.forEach(System.out::println);
                            break;

                        case REVIEWS:
                            timelines = new  ArrayList<LinkedHashMap<String, Object>>();      
                            responseContent = (Map<String, Object>) decryptContent(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));                           
                            List<LinkedHashMap<String, Object>> recvReviews = (List<LinkedHashMap<String, Object>>) responseContent.get("reviews");
                           
                            if (recvReviews != null)
                            {   
                                for (LinkedHashMap<String, Object> review : recvReviews)
                                {
                                    client.getTimeLineByReviewId(Long.valueOf((int)review.get("id")));
                                }
                                
                                
                              
                               client.setReviewsList((ArrayList<LinkedHashMap<String, Object>>) recvReviews);
                               client.setTimelineList((ArrayList<LinkedHashMap<String, Object>>) this.timelines);
                            }    
                            
                            break;
                        case TIMELINE_BY_REVIEWID:
                            responseContent = (Map<String, Object>) decryptContent(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));
                            LinkedHashMap<String, Object> recvTimeline = (LinkedHashMap<String, Object>) responseContent.get("timeline");

                            timelines.add(recvTimeline);
                            break;   
                        
                        case CATEGORIES:
                            responseContent = (Map<String, Object>) decryptContent(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));

                            List<LinkedHashMap<String, Object>> recvCategories = (List<LinkedHashMap<String, Object>>) responseContent.get("categories");
                            System.out.println("Client receive: ");
                           // recvCategories.forEach(System.out::println);
                            client.updateComboboxCategory(recvCategories);
                            break;
                        case BRANDS_BY_CATEGORY_ID:
                            responseContent = (Map<String, Object>) decryptContent(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));
                            if(responseContent.get("brands")!= null)
                            {
                                List<LinkedHashMap<String, Object>> recvBrands = (List<LinkedHashMap<String, Object>>) responseContent.get("brands");
                                client.updateBrands(recvBrands);
                            }
                            break;

//                        case ADVANCE_CATEGORIES:
//                            responseContent = (Map<String, Object>) decryptContent(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));
//
//                            List<LinkedHashMap<String, Object>> recvCategories = (List<LinkedHashMap<String, Object>>) responseContent.get("advance_category");
//                            System.out.println("Client receive: ");
//                           // recvCategories.forEach(System.out::println);
//                            client.updateComboboxCategory(recvCategories);
//                            break;
                            
                        case USER_DISCONNECT:
                            isRunning = false;
                            in.close();
                            break;

                        case ERROR:
                            responseContent = (Map<String, Object>) decryptContent(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));

                            String error = (String) responseContent.get("error");
                            System.out.println("Client receive: " + error);
                            break;
                            
                        case ADVANCE_PRODUCTS:
                            responseContent = (Map<String, Object>) decryptContent(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));
                            List<List<LinkedHashMap<String, Object>>> listAdvanceProducts = (List<List<LinkedHashMap<String, Object>>>) responseContent.get("list_advance_products");
                            client.setTable(listAdvanceProducts);
                            break;

                        default:
                            System.out.println("Not supported yet!");
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object decryptContent(SecretKey secretKey, byte[] content) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IOException {
        byte[] ivBytes = Arrays.copyOfRange(content, 0, 16);
        byte[] contentInBytes = Arrays.copyOfRange(content, 16, content.length);

        IvParameterSpec ivParams = AESUtil.getIVParams(ivBytes);

        byte[] decryptedContent = AESUtil.decrypt(secretKey, ivParams, contentInBytes);
        String contentInJSON = (String) BytesUtil.encode(decryptedContent);

        return new ObjectMapper().readValue(contentInJSON, Map.class);
    }
}
