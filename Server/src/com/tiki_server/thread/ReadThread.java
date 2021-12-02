package com.tiki_server.thread;

import com.tiki_server.dto.ProductDTO;
import com.tiki_server.model.Message;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReadThread implements Runnable {
    private final byte[] recvBuf = new byte[100000];

    private DatagramSocket socket = null;
    private DatagramPacket dpreceive = null;

    private ByteArrayInputStream byteInputStream = null;
    private ObjectInput oi = null;

    public ReadThread(DatagramSocket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while(true) {
//            Get response from server
            Message responseMsg = receiveResponse();

            ProductDTO recvProduct = null;

            if (responseMsg != null) {
                switch (responseMsg.getMessageType()) {
                    case PRODUCT_INFO:
                        recvProduct = (ProductDTO) responseMsg.getContent().get("product");
                        System.out.println("Client receive: " + recvProduct);
                        break;
                    case PRODUCTS:
                        recvProduct = (ProductDTO) responseMsg.getContent().get("products");
                        System.out.println("Client receive: " + recvProduct);
                        break;
                    case ERROR:
                        String error = (String) responseMsg.getContent().get("error");
                        System.out.println("Client receive: " + error);
                        break;
                    default:
                        System.out.println("Not supported yet!");
                        break;
                }
            }
        }
    }

    //        Receive response from server
    private Message receiveResponse() {
        Message response = null;
        try
        {
            dpreceive = new DatagramPacket(recvBuf, recvBuf.length);
            socket.receive(dpreceive);

            byteInputStream = new ByteArrayInputStream(recvBuf);
            oi = new ObjectInputStream(new BufferedInputStream(byteInputStream));

            response = (Message) oi.readObject();
            oi.close();

            return response;
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
