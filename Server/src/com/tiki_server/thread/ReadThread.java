package com.tiki_server.thread;

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
import java.util.Arrays;
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
                    String encryptedResponseContent = (String) response.getContent();
                    Map<String, Object> responseContent;

                    switch (response.getMessageType()) {
                        case PUBLIC_KEY:
                            responseContent = (Map<String, Object>) BytesUtil.encode(Base64.getDecoder().decode(encryptedResponseContent));
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
                            responseContent = (Map<String, Object>) decryptMessage(client.getSecretKey(), Base64.getDecoder().decode(encryptedResponseContent));

                            ProductDTO recvProduct = (ProductDTO) responseContent.get("product");
                            System.out.println("Client receive: " + recvProduct);
                            break;
                        case PRODUCTS:
                            responseContent = (Map<String, Object>) decryptMessage(client.getSecretKey(), Base64.getDecoder().decode(encryptedResponseContent));

                            recvProduct = (ProductDTO) responseContent.get("product");
                            System.out.println("Client receive: " + recvProduct);
                            break;
                        case CONFIGURABLE_PRODUCTS:
                            responseContent = (Map<String, Object>) decryptMessage(client.getSecretKey(), Base64.getDecoder().decode(encryptedResponseContent));

                            ConfigurableProductDTO recvCP = (ConfigurableProductDTO) responseContent.get("configurableProduct");
                            System.out.println("Client receive: " + recvCP);
                            break;
                        case PRODUCT_HISTORIES:
                            responseContent = (Map<String, Object>) decryptMessage(client.getSecretKey(), Base64.getDecoder().decode(encryptedResponseContent));

                            HistoryDTO recvProductHistory = (HistoryDTO) responseContent.get("productHistory");
                            System.out.println("Client receive: " + recvProductHistory);
                            break;
                        case CONFIGURABLE_PRODUCT_HISTORIES:
                            responseContent = (Map<String, Object>) decryptMessage(client.getSecretKey(), Base64.getDecoder().decode(encryptedResponseContent));

                            ConfigurableProductHistoryDTO recvCPHistory = (ConfigurableProductHistoryDTO) responseContent.get("configurableProductHistory");
                            System.out.println("Client receive: " + recvCPHistory);
                            break;
                        case REVIEWS:
                            responseContent = (Map<String, Object>) decryptMessage(client.getSecretKey(), Base64.getDecoder().decode(encryptedResponseContent));

                            ReviewDTO recvReview = (ReviewDTO) responseContent.get("review");
                            System.out.println("Client receive: " + recvReview);
                            break;
                        case USER_DISCONNECT:
                            isRunning = false;
                            in.close();
                            break;
                        case ERROR:
                            responseContent = (Map<String, Object>) decryptMessage(client.getSecretKey(), Base64.getDecoder().decode(encryptedResponseContent));

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

    public Object decryptMessage(SecretKey secretKey, byte[] content) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        byte[] ivBytes = Arrays.copyOfRange(content, 0, 16);
        byte[] contentInBytes = Arrays.copyOfRange(content, 16, content.length);

        IvParameterSpec ivParams = AESUtil.getIVParams(ivBytes);

        byte[] decryptedContent = AESUtil.decrypt(secretKey, ivParams, contentInBytes);

        return BytesUtil.encode(decryptedContent);
    }
}
