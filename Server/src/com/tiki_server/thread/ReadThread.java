package com.tiki_server.thread;

import com.tiki_server.dto.*;
import com.tiki_server.model.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ReadThread implements Runnable {
    private volatile boolean isRunning = true;

    private Socket socket;

    private ByteArrayInputStream byteInputStream = null;
    private ObjectInput in = null;

    public ReadThread(Socket socket) throws IOException {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            this.in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            Message responseMsg;
            while(isRunning) {
//            Get response from server
                responseMsg = (Message) in.readObject();

                if (responseMsg != null) {
                    switch (responseMsg.getMessageType()) {
                        case PRODUCT_INFO:
                            ProductDTO recvProduct = (ProductDTO) responseMsg.getContent().get("product");
                            System.out.println("Client receive: " + recvProduct);
                            break;
                        case PRODUCTS:
                            recvProduct = (ProductDTO) responseMsg.getContent().get("product");
                            System.out.println("Client receive: " + recvProduct);
                            break;
                        case CONFIGURABLE_PRODUCTS:
                            ConfigurableProductDTO recvCP = (ConfigurableProductDTO) responseMsg.getContent().get("configurableProduct");
                            System.out.println("Client receive: " + recvCP);
                            break;
                        case PRODUCT_HISTORIES:
                            HistoryDTO recvProductHistory = (HistoryDTO) responseMsg.getContent().get("productHistory");
                            System.out.println("Client receive: " + recvProductHistory);
                            break;
                        case CONFIGURABLE_PRODUCT_HISTORIES:
                            ConfigurableProductHistoryDTO recvCPHistory = (ConfigurableProductHistoryDTO) responseMsg.getContent().get("configurableProductHistory");
                            System.out.println("Client receive: " + recvCPHistory);
                            break;
                        case REVIEWS:
                            ReviewDTO recvReview = (ReviewDTO) responseMsg.getContent().get("review");
                            System.out.println("Client receive: " + recvReview);
                            break;
                        case USER_DISCONNECT:
                            isRunning = false;
                            in.close();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
