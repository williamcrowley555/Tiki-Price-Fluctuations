package com.tiki_server.thread;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiki_server.dto.*;
import com.tiki_server.enums.MessageType;
import com.tiki_server.main.Client;
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
                            responseContent = (Map<String, Object>) decryptMessage(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));

                            System.out.println(responseContent.get("product"));

                            ProductDTO recvProduct = (ProductDTO) responseContent.get("product");
                            System.out.println("Client receive: " + recvProduct);
                            break;

                        case PRODUCTS:
                            responseContent = (Map<String, Object>) decryptMessage(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));

                            List<ProductDTO> recvProducts = (List<ProductDTO>) responseContent.get("products");
                            System.out.println("Client receive: ");
                            recvProducts.forEach(System.out::println);
                            break;

                        case CONFIGURABLE_PRODUCTS:
                            responseContent = (Map<String, Object>) decryptMessage(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));

                            List<ConfigurableProductDTO> recvCPs = (List<ConfigurableProductDTO>) responseContent.get("configurableProducts");
                            System.out.println("Client receive: ");
                            recvCPs.forEach(System.out::println);
                            break;

                        case PRODUCT_HISTORIES:
                            responseContent = (Map<String, Object>) decryptMessage(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));

                            List<HistoryDTO> recvProductHistories = (List<HistoryDTO>) responseContent.get("productHistories");
                            System.out.println("Client receive: ");
                            recvProductHistories.forEach(System.out::println);
                            break;

                        case CONFIGURABLE_PRODUCT_HISTORIES:
                            responseContent = (Map<String, Object>) decryptMessage(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));

                            List<ConfigurableProductHistoryDTO> recvCPHistories = (List<ConfigurableProductHistoryDTO>) responseContent.get("configurableProductHistories");
                            System.out.println("Client receive: ");
                            recvCPHistories.forEach(System.out::println);
                            break;

                        case REVIEWS:
                            responseContent = (Map<String, Object>) decryptMessage(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));

                            List<ReviewDTO> recvReviews = (List<ReviewDTO>) responseContent.get("reviews");
                            System.out.println("Client receive: ");
                            recvReviews.forEach(System.out::println);
                            break;

                        case USER_DISCONNECT:
                            isRunning = false;
                            in.close();
                            break;

                        case ERROR:
                            responseContent = (Map<String, Object>) decryptMessage(client.getSecretKey(), Base64.getDecoder().decode(encryptedContent));

                            String error = (String) responseContent.get("error");
                            System.out.println("Client receive: " + error);
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

    public Object decryptMessage(SecretKey secretKey, byte[] content) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IOException {
        byte[] ivBytes = Arrays.copyOfRange(content, 0, 16);
        byte[] contentInBytes = Arrays.copyOfRange(content, 16, content.length);

        IvParameterSpec ivParams = AESUtil.getIVParams(ivBytes);

        byte[] decryptedContent = AESUtil.decrypt(secretKey, ivParams, contentInBytes);
        String contentInJSON = (String) BytesUtil.encode(decryptedContent);

        return new ObjectMapper().readValue(contentInJSON, Map.class);
    }
}
