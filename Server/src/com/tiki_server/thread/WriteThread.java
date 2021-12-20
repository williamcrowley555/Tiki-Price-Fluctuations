package com.tiki_server.thread;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiki_server.enums.MessageType;
import com.tiki_server.main.Client;
import com.tiki_server.model.Message;
import com.tiki_server.util.AESUtil;
import com.tiki_server.util.BytesUtil;
import com.tiki_server.util.RSAUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class WriteThread implements Runnable {
    private Client client;
    private Socket socket;

    private ByteArrayOutputStream byteOutputStream = null;
    private ObjectOutput out = null;

    private Message message;

    public WriteThread(Client client, Socket socket, ObjectOutput out, Message message) throws IOException {
        this.client = client;
        this.socket = socket;
        this.out = out;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            sendRequest(this.message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
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
        }
    }

//    Send request to server
    public void sendRequest(Message request) throws IOException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String reqContentInJSON = new ObjectMapper().writeValueAsString(request.getContent());
        byte[] reqContentInBytes = BytesUtil.decode(reqContentInJSON);

        if (request.getMessageType().equals(MessageType.SEND_SECRET_KEY)) {
//            Encrypt AES key with Public key
            byte[] encryptedKey = RSAUtil.encrypt(client.getPublicKey(), reqContentInBytes);
            String content = Base64.getEncoder().encodeToString(encryptedKey);

            request.setContent(content);
        } else {
            byte[] encryptedMessage = AESUtil.encrypt(this.client.getSecretKey(), reqContentInBytes);
            String content = Base64.getEncoder().encodeToString(encryptedMessage);

            request.setContent(content);
        }

        String json = new ObjectMapper().writeValueAsString(request);

        out.writeObject(json);
        out.flush();
    }
}
