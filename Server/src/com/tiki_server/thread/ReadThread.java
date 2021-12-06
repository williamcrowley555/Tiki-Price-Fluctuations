package com.tiki_server.thread;

import com.tiki_server.dto.*;
import com.tiki_server.enums.MessageType;
import com.tiki_server.main.Client;
import com.tiki_server.model.Message;
import com.tiki_server.util.AESUtil;
import com.tiki_server.util.BytesUtil;
import com.tiki_server.util.RSAUtil;

import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

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
            Message response;

            while(isRunning) {
//            Get response from server
                response = (Message) in.readObject();

                if (response != null) {
                    String responseContent = (String) response.getContent();
                    byte[] msgContentInBytes;

//                    switch (response.getMessageType()) {
//                        case PUBLIC_KEY:
//                            Map<String, Object> content = (Map<String, Object>) BytesUtil.encode(Base64.getDecoder().decode(responseContent));
//                            String strPublicKey = (String) content.get("strPublicKey");
//                            PublicKey publicKey = RSAUtil.getPublicKey(Base64.getDecoder().decode(strPublicKey));
//                            this.client.setPublicKey(publicKey);
//
////                            Generate AES key
//                            SecretKey secretKey = AESUtil.generateAESKey();
//                            this.client.setSecretKey(secretKey);
//                            String strSecretKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//
//                            Map<String, Object> msgContent = new HashMap<>();
//                            msgContent.put("strSecretKey", strSecretKey);
//
//                            this.client.sendMessage(new Message(msgContent, MessageType.SEND_SECRET_KEY));
//                            break;
//
//                        case PRODUCT_INFO:
//                            msgContentInBytes = AESUtil.decrypt(this.client.getSecretKey(), Base64.getDecoder().decode(responseContent));
//                            content = (Map<String, Object>) BytesUtil.encode(msgContentInBytes);
//
//                            ProductDTO recvProduct = (ProductDTO) content.get("product");
//                            System.out.println("Client receive: " + recvProduct);
//                            break;
//                        case PRODUCTS:
//                            msgContentInBytes = AESUtil.decrypt(this.client.getSecretKey(), Base64.getDecoder().decode(responseContent));
//                            content = (Map<String, Object>) BytesUtil.encode(msgContentInBytes);
//
//                            recvProduct = (ProductDTO) content.get("product");
//                            System.out.println("Client receive: " + recvProduct);
//                            break;
//                        case CONFIGURABLE_PRODUCTS:
//                            msgContentInBytes = AESUtil.decrypt(this.client.getSecretKey(), Base64.getDecoder().decode(responseContent));
//                            content = (Map<String, Object>) BytesUtil.encode(msgContentInBytes);
//
//                            ConfigurableProductDTO recvCP = (ConfigurableProductDTO) content.get("configurableProduct");
//                            System.out.println("Client receive: " + recvCP);
//                            break;
//                        case PRODUCT_HISTORIES:
//                            msgContentInBytes = AESUtil.decrypt(this.client.getSecretKey(), Base64.getDecoder().decode(responseContent));
//                            content = (Map<String, Object>) BytesUtil.encode(msgContentInBytes);
//
//                            HistoryDTO recvProductHistory = (HistoryDTO) content.get("productHistory");
//                            System.out.println("Client receive: " + recvProductHistory);
//                            break;
//                        case CONFIGURABLE_PRODUCT_HISTORIES:
//                            msgContentInBytes = AESUtil.decrypt(this.client.getSecretKey(), Base64.getDecoder().decode(responseContent));
//                            content = (Map<String, Object>) BytesUtil.encode(msgContentInBytes);
//
//                            ConfigurableProductHistoryDTO recvCPHistory = (ConfigurableProductHistoryDTO) content.get("configurableProductHistory");
//                            System.out.println("Client receive: " + recvCPHistory);
//                            break;
//                        case REVIEWS:
//                            msgContentInBytes = AESUtil.decrypt(this.client.getSecretKey(), Base64.getDecoder().decode(responseContent));
//                            content = (Map<String, Object>) BytesUtil.encode(msgContentInBytes);
//
//                            ReviewDTO recvReview = (ReviewDTO) content.get("review");
//                            System.out.println("Client receive: " + recvReview);
//                            break;
//                        case USER_DISCONNECT:
//                            isRunning = false;
//                            in.close();
//                            break;
//                        case ERROR:
//                            msgContentInBytes = AESUtil.decrypt(this.client.getSecretKey(), Base64.getDecoder().decode(responseContent));
//                            content = (Map<String, Object>) BytesUtil.encode(msgContentInBytes);
//
//                            String error = (String) content.get("error");
//                            System.out.println("Client receive: " + error);
//                            break;
//                        default:
//                            System.out.println("Not supported yet!");
//                            break;
//                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
