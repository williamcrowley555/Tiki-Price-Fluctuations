package com.tiki_server.thread;

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
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Message clientRequest = null;
        try {
            this.in = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            this.out = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

            while (isRunning) {
                clientRequest = (Message) in.readObject();
                if (clientRequest != null) {
                    String requestContent = (String) clientRequest.getContent();
                    byte[] msgContentInBytes;
                    Message response = null;
                    Map<String, Object> responseContent = null;

                    IProductBLL productBLL = null;
                    IConfigurableProductBLL cpBLL = null;
                    IHistoryBLL historyBLL = null;
                    IConfigurableProductHistoryBLL cpHistoryBLL = null;
                    IReviewBLL reviewBLL = null;

//                    switch (clientRequest.getMessageType()) {
//                        case GET_PUBLIC_KEY:
//                            sendPublicKey();
//                            break;
//
//                        case SEND_SECRET_KEY:
//                            msgContentInBytes = RSAUtil.decryptKey(privateKey,  Base64.getDecoder().decode(requestContent));
//                            Map<String, Object> content = (Map<String, Object>) BytesUtil.encode(msgContentInBytes);
//
//                            String strSecretKey = (String) content.get("strSecretKey");
//                            secretKey = AESUtil.getAESKey(Base64.getDecoder().decode(strSecretKey));
//                            break;
//
//                        case GET_PRODUCT:
//                            msgContentInBytes = AESUtil.decrypt(secretKey, requestContent.getBytes());
//                            content = (Map<String, Object>) BytesUtil.encode(msgContentInBytes);
//                            Long productId = (Long) content.get("productId");
//
//                            productBLL = new ProductBLL();
//                            ProductDTO product = productBLL.findById(productId);
//
//                            responseContent = new HashMap<>();
//                            responseContent.put("product", product);
//
//                            response = new Message(responseContent, MessageType.PRODUCT_INFO);
//                            sendMessage(response);
//                            break;
//
//                        case FILTER_PRODUCTS:
//                            msgContentInBytes = AESUtil.decrypt(secretKey, requestContent.getBytes());
//                            content = (Map<String, Object>) BytesUtil.encode(msgContentInBytes);
//
//                            if (content == null)
//                                response = createErrorMessage("Content can not be null");
//                            else {
//                                String productName = content.containsKey("productName") ? (String) content.get("productName") : null;
//                                Long categoryId = content.containsKey("categoryId") ? (Long) content.get("categoryId") : null;
//                                Long brandId = content.containsKey("brandId") ? (Long) content.get("brandId") : null;
//                                Float ratingAverage = content.containsKey("ratingAverage") ? (Float) content.get("ratingAverage") : null;
//                                Long minPrice = content.containsKey("minPrice") ? (Long) content.get("minPrice") : null;
//                                Long maxPrice = content.containsKey("maxPrice") ? (Long) content.get("maxPrice") : null;
//
//                                productBLL = new ProductBLL();
//                                List<ProductDTO> products = productBLL.filter(productName, categoryId, brandId, ratingAverage, minPrice, maxPrice);
//
//                                responseContent = new HashMap<>();
//
//                                if (products.isEmpty()) {
//                                    responseContent.put("product", null);
//                                    response = new Message(responseContent, MessageType.PRODUCTS);
//                                    sendMessage(response);
//                                } else {
//                                    for (ProductDTO p : products) {
//                                        responseContent.put("product", p);
//                                        response = new Message(responseContent, MessageType.PRODUCTS);
//                                        sendMessage(response);
//                                    }
//                                }
//                            }
//                            break;
//
//                        case GET_CONFIGURABLE_PRODUCTS:
//                            msgContentInBytes = AESUtil.decrypt(secretKey, requestContent.getBytes());
//                            content = (Map<String, Object>) BytesUtil.encode(msgContentInBytes);
//
//                            productId = (Long) content.get("productId");
//
//                            cpBLL = new ConfigurableProductBLL();
//                            List<ConfigurableProductDTO> cps = cpBLL.findByProductId(productId);
//
//                            responseContent = new HashMap<>();
//
//                            if (cps.isEmpty()) {
//                                responseContent.put("configurableProduct", null);
//                                response = new Message(responseContent, MessageType.CONFIGURABLE_PRODUCTS);
//                                sendMessage(response);
//                            } else {
//                                for (ConfigurableProductDTO cp : cps) {
//                                    responseContent.put("configurableProduct", cp);
//                                    response = new Message(responseContent, MessageType.CONFIGURABLE_PRODUCTS);
//                                    sendMessage(response);
//                                }
//                            }
//                            break;
//
//                        case GET_PRODUCT_HISTORIES_BY_URL:
//                            msgContentInBytes = AESUtil.decrypt(secretKey, requestContent.getBytes());
//                            content = (Map<String, Object>) BytesUtil.encode(msgContentInBytes);
//
//                            String url = (String) content.get("url");
//                            int month = (int) content.get("month");
//                            int year = (int) content.get("year");
//
//                            historyBLL = new HistoryBLL();
//                            List<HistoryDTO> histories = historyBLL.findByProductPageUrl(url, month, year);
//
//                            responseContent = new HashMap<>();
//
//                            if (histories.isEmpty()) {
//                                responseContent.put("productHistory", null);
//                                response = new Message(responseContent, MessageType.PRODUCT_HISTORIES);
//                                sendMessage(response);
//                            } else {
//                                for (HistoryDTO history : histories) {
//                                    responseContent.put("productHistory", history);
//                                    response = new Message(responseContent, MessageType.PRODUCT_HISTORIES);
//                                    sendMessage(response);
//                                }
//                            }
//                            break;
//
//                        case GET_PRODUCT_HISTORIES_BY_PRODUCT_ID:
//                            msgContentInBytes = AESUtil.decrypt(secretKey, requestContent.getBytes());
//                            content = (Map<String, Object>) BytesUtil.encode(msgContentInBytes);
//
//                            productId = (Long) content.get("productId");
//                            month = (int) content.get("month");
//                            year = (int) content.get("year");
//
//                            historyBLL = new HistoryBLL();
//                            histories = historyBLL.findByProductId(productId, month, year);
//
//                            responseContent = new HashMap<>();
//
//                            if (histories.isEmpty()) {
//                                responseContent.put("productHistory", null);
//                                response = new Message(responseContent, MessageType.PRODUCT_HISTORIES);
//                                sendMessage(response);
//                            } else {
//                                for (HistoryDTO history : histories) {
//                                    responseContent.put("productHistory", history);
//                                    response = new Message(responseContent, MessageType.PRODUCT_HISTORIES);
//                                    sendMessage(response);
//                                }
//                            }
//                            break;
//
//                        case GET_CONFIGURABLE_PRODUCT_HISTORIES:
//                            msgContentInBytes = AESUtil.decrypt(secretKey, requestContent.getBytes());
//                            content = (Map<String, Object>) BytesUtil.encode(msgContentInBytes);
//
//                            productId = (Long) content.get("productId");
//                            Long cpId = (Long) content.get("cpId");
//                            month = (int) content.get("month");
//                            year = (int) content.get("year");
//
//                            cpHistoryBLL = new ConfigurableProductHistoryBLL();
//                            List<ConfigurableProductHistoryDTO> cpHistories = cpHistoryBLL.findByProductIdAndConfigurableProductId(productId, cpId, month, year);
//
//                            responseContent = new HashMap<>();
//
//                            if (cpHistories.isEmpty()) {
//                                responseContent.put("configurableProductHistory", null);
//                                response = new Message(responseContent, MessageType.CONFIGURABLE_PRODUCT_HISTORIES);
//                                sendMessage(response);
//                            } else {
//                                for (ConfigurableProductHistoryDTO cpHistory : cpHistories) {
//                                    responseContent.put("configurableProductHistory", cpHistory);
//                                    response = new Message(responseContent, MessageType.CONFIGURABLE_PRODUCT_HISTORIES);
//                                    sendMessage(response);
//                                }
//                            }
//                            break;
//
//                        case GET_REVIEWS_BY_PRODUCT_ID:
//                            msgContentInBytes = AESUtil.decrypt(secretKey, requestContent.getBytes());
//                            content = (Map<String, Object>) BytesUtil.encode(msgContentInBytes);
//
//                            productId = (Long) content.get("productId");
//
//                            reviewBLL = new ReviewBLL();
//                            List<ReviewDTO> reviews = reviewBLL.findByProductId(productId);
//
//                            responseContent = new HashMap<>();
//
//                            if (reviews.isEmpty()) {
//                                responseContent.put("review", null);
//                                response = new Message(responseContent, MessageType.REVIEWS);
//                                sendMessage(response);
//                            } else {
//                                for (ReviewDTO review : reviews) {
//                                    responseContent.put("review", review);
//                                    response = new Message(responseContent, MessageType.REVIEWS);
//                                    sendMessage(response);
//                                }
//                            }
//                            break;
//
//                        case USER_DISCONNECT:
//                            isRunning = false;
//                            response = new Message(null, MessageType.USER_DISCONNECT);
//                            sendMessage(response);
//                            break;
//
//                        default:
//                            response = createErrorMessage("Invalid Message!");
//                            sendMessage(response);
//                            break;
//                    }
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
//        if (message.getMessageType().equals(MessageType.PUBLIC_KEY)) {
//            byte[] msgContentInBytes = BytesUtil.decode(message.getContent());
//            String content = Base64.getEncoder().encodeToString(msgContentInBytes);
//
//            message.setContent(content);
//        } else {
//            try {
//                byte[] msgContentInBytes = BytesUtil.decode(message.getContent());
//
//                if (this.secretKey != null) {
//                    byte[] encryptedMsgContent = AESUtil.encrypt(this.secretKey, msgContentInBytes);
//                    String content = Base64.getEncoder().encodeToString(encryptedMsgContent);
//
//                    message.setContent(encryptedMsgContent);
//                } else {
//                    System.err.println("Error: No Secret Key Found");
//                    return;
//                }
//            } catch (NoSuchPaddingException e) {
//                e.printStackTrace();
//            } catch (IllegalBlockSizeException e) {
//                e.printStackTrace();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (BadPaddingException e) {
//                e.printStackTrace();
//            } catch (InvalidKeyException e) {
//                e.printStackTrace();
//            }
//        }

        out.writeObject(message);
        out.flush();
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
