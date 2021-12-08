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
        Message clientRequest = null;
        try {
            this.in = new ObjectInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            this.out = new ObjectOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));

            while (isRunning) {
                clientRequest = (Message) in.readObject();
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

                    switch (clientRequest.getMessageType()) {
                        case GET_PUBLIC_KEY:
                            sendPublicKey();
                            break;

                        case SEND_SECRET_KEY:
                            byte[] msgContentInBytes = RSAUtil.decrypt(privateKey,  Base64.getDecoder().decode(encryptedRequestContent));
                            requestContent = (Map<String, Object>) BytesUtil.encode(msgContentInBytes);

                            String strSecretKey = (String) requestContent.get("strSecretKey");
                            secretKey = AESUtil.getAESKey(Base64.getDecoder().decode(strSecretKey));
                            break;

                        case GET_PRODUCT:
                            requestContent = (Map<String, Object>) decryptMessage(secretKey, Base64.getDecoder().decode(encryptedRequestContent));
                            Long productId = (Long) requestContent.get("productId");

                            productBLL = new ProductBLL();
                            ProductDTO product = productBLL.findById(productId);

                            responseContent = new HashMap<>();
                            responseContent.put("product", product);

                            response = new Message(responseContent, MessageType.PRODUCT_INFO);
                            sendMessage(response);
                            break;

                        case FILTER_PRODUCTS:
                            requestContent = (Map<String, Object>) decryptMessage(secretKey, Base64.getDecoder().decode(encryptedRequestContent));

                            if (requestContent == null) {
                                response = createErrorMessage("Content can not be null");
                                sendMessage(response);
                            }
                            else {
                                String productName = requestContent.containsKey("productName") ? (String) requestContent.get("productName") : null;
                                Long categoryId = requestContent.containsKey("categoryId") ? (Long) requestContent.get("categoryId") : null;
                                Long brandId = requestContent.containsKey("brandId") ? (Long) requestContent.get("brandId") : null;
                                Float ratingAverage = requestContent.containsKey("ratingAverage") ? (Float) requestContent.get("ratingAverage") : null;
                                Long minPrice = requestContent.containsKey("minPrice") ? (Long) requestContent.get("minPrice") : null;
                                Long maxPrice = requestContent.containsKey("maxPrice") ? (Long) requestContent.get("maxPrice") : null;

                                productBLL = new ProductBLL();
                                List<ProductDTO> products = productBLL.filter(productName, categoryId, brandId, ratingAverage, minPrice, maxPrice);

                                responseContent = new HashMap<>();

                                if (products.isEmpty())
                                    products = null;

                                responseContent.put("products", products);
                                response = new Message(responseContent, MessageType.PRODUCTS);
                                sendMessage(response);
                            }
                            break;

                        case GET_CONFIGURABLE_PRODUCTS:
                            requestContent = (Map<String, Object>) decryptMessage(secretKey, Base64.getDecoder().decode(encryptedRequestContent));

                            productId = (Long) requestContent.get("productId");

                            cpBLL = new ConfigurableProductBLL();
                            List<ConfigurableProductDTO> configurableProducts = cpBLL.findByProductId(productId);

                            responseContent = new HashMap<>();

                            if (configurableProducts.isEmpty())
                                configurableProducts = null;

                            responseContent.put("configurableProducts", configurableProducts);
                            response = new Message(responseContent, MessageType.CONFIGURABLE_PRODUCTS);
                            sendMessage(response);
                            break;

                        case GET_PRODUCT_HISTORIES_BY_URL:
                            requestContent = (Map<String, Object>) decryptMessage(secretKey, Base64.getDecoder().decode(encryptedRequestContent));

                            String url = (String) requestContent.get("url");
                            int month = (int) requestContent.get("month");
                            int year = (int) requestContent.get("year");

                            historyBLL = new HistoryBLL();
                            List<HistoryDTO> histories = historyBLL.findByProductPageUrl(url, month, year);

                            responseContent = new HashMap<>();

                            if (histories.isEmpty())
                                histories = null;

                            responseContent.put("productHistories", histories);
                            response = new Message(responseContent, MessageType.PRODUCT_HISTORIES);
                            sendMessage(response);
                            break;

                        case GET_PRODUCT_HISTORIES_BY_PRODUCT_ID:
                            requestContent = (Map<String, Object>) decryptMessage(secretKey, Base64.getDecoder().decode(encryptedRequestContent));

                            productId = (Long) requestContent.get("productId");
                            month = (int) requestContent.get("month");
                            year = (int) requestContent.get("year");

                            historyBLL = new HistoryBLL();
                            histories = historyBLL.findByProductId(productId, month, year);

                            responseContent = new HashMap<>();

                            if (histories.isEmpty()) {
                                histories = null;
                            }

                            responseContent.put("productHistories", histories);
                            response = new Message(responseContent, MessageType.PRODUCT_HISTORIES);
                            sendMessage(response);
                            break;

                        case GET_CONFIGURABLE_PRODUCT_HISTORIES:
                            requestContent = (Map<String, Object>) decryptMessage(secretKey, Base64.getDecoder().decode(encryptedRequestContent));

                            productId = (Long) requestContent.get("productId");
                            Long cpId = (Long) requestContent.get("cpId");
                            month = (int) requestContent.get("month");
                            year = (int) requestContent.get("year");

                            cpHistoryBLL = new ConfigurableProductHistoryBLL();
                            List<ConfigurableProductHistoryDTO> cpHistories = cpHistoryBLL.findByProductIdAndConfigurableProductId(productId, cpId, month, year);

                            responseContent = new HashMap<>();

                            if (cpHistories.isEmpty())
                                cpHistories = null;

                            responseContent.put("configurableProductHistories", cpHistories);
                            response = new Message(responseContent, MessageType.CONFIGURABLE_PRODUCT_HISTORIES);
                            sendMessage(response);
                            break;

                        case GET_REVIEWS_BY_PRODUCT_ID:
                            requestContent = (Map<String, Object>) decryptMessage(secretKey, Base64.getDecoder().decode(encryptedRequestContent));

                            productId = (Long) requestContent.get("productId");

                            reviewBLL = new ReviewBLL();
                            List<ReviewDTO> reviews = reviewBLL.findByProductId(productId);

                            responseContent = new HashMap<>();

                            if (reviews.isEmpty())
                                reviews = null;

                            responseContent.put("reviews", reviews);
                            response = new Message(responseContent, MessageType.REVIEWS);
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
        byte[] msgContentInBytes = BytesUtil.decode(message.getContent());

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

        out.writeObject(message);
        out.flush();
    }

    public Object decryptMessage(SecretKey secretKey, byte[] content) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        byte[] ivBytes = Arrays.copyOfRange(content, 0, 16);
        byte[] contentInBytes = Arrays.copyOfRange(content, 16, content.length);

        IvParameterSpec ivParams = AESUtil.getIVParams(ivBytes);

        byte[] decryptedContent = AESUtil.decrypt(secretKey, ivParams, contentInBytes);

        return BytesUtil.encode(decryptedContent);
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
