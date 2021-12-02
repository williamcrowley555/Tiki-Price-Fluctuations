package com.tiki_server.main;

import com.tiki_server.bll.*;
import com.tiki_server.bll.impl.*;
import com.tiki_server.dto.*;
import com.tiki_server.enums.MessageType;
import com.tiki_server.model.Message;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private int port = 1234;
    private int bufsize = 100000;

    private DatagramSocket socket = null;
    private DatagramPacket dpreceive = null;
    private DatagramPacket dpsend = null;

    private ByteArrayOutputStream byteOutputStream = null;
    private ByteArrayInputStream byteInputStream = null;
    private ObjectOutput oo = null;
    private ObjectInput oi = null;

    public void run() {
        try {
            socket = new DatagramSocket(port);
            System.out.println("Server started!");

            while(true) {
                Message clientRequest = receiveMessage();

                if (clientRequest != null) {
                    Map<String, Object> requestContent = clientRequest.getContent();;
                    Message response = null;
                    Map<String, Object> responseContent = null;

                    IProductBLL productBLL = null;
                    IConfigurableProductBLL cpBLL = null;
                    IHistoryBLL historyBLL = null;
                    IConfigurableProductHistoryBLL cpHistoryBLL = null;
                    IReviewBLL reviewBLL = null;

                    switch (clientRequest.getMessageType()) {
                        case GET_PRODUCT:
                            Long productId = (Long) requestContent.get("productId");

                            productBLL = new ProductBLL();
                            ProductDTO product = productBLL.findById(productId);

                            responseContent = new HashMap<>();
                            responseContent.put("product", product);

                            response = new Message(responseContent, MessageType.PRODUCT_INFO);

                            sendMessage(response);
                            break;

                        case FILTER_PRODUCTS:
                            if (requestContent == null)
                                response = createErrorMessage("Content can not be null");
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

                                if (products.isEmpty()) {
                                    responseContent.put("product", null);
                                    response = new Message(responseContent, MessageType.PRODUCTS);
                                    sendMessage(response);
                                } else {
                                    for (ProductDTO p : products) {
                                        responseContent.put("product", p);
                                        response = new Message(responseContent, MessageType.PRODUCTS);
                                        sendMessage(response);
                                    }
                                }
                            }
                            break;

                        case GET_CONFIGURABLE_PRODUCTS:
                            productId = (Long) requestContent.get("productId");

                            cpBLL = new ConfigurableProductBLL();
                            List<ConfigurableProductDTO> cps = cpBLL.findByProductId(productId);

                            responseContent = new HashMap<>();

                            if (cps.isEmpty()) {
                                responseContent.put("configurableProduct", null);
                                response = new Message(responseContent, MessageType.CONFIGURABLE_PRODUCTS);
                                sendMessage(response);
                            } else {
                                for (ConfigurableProductDTO cp : cps) {
                                    responseContent.put("configurableProduct", cp);
                                    response = new Message(responseContent, MessageType.CONFIGURABLE_PRODUCTS);
                                    sendMessage(response);
                                }
                            }
                            break;

                        case GET_PRODUCT_HISTORIES_BY_URL:
                            String url = (String) requestContent.get("url");
                            int month = (int) requestContent.get("month");
                            int year = (int) requestContent.get("year");

                            historyBLL = new HistoryBLL();
                            List<HistoryDTO> histories = historyBLL.findByProductPageUrl(url, month, year);

                            responseContent = new HashMap<>();

                            if (histories.isEmpty()) {
                                responseContent.put("productHistory", null);
                                response = new Message(responseContent, MessageType.PRODUCT_HISTORIES);
                                sendMessage(response);
                            } else {
                                for (HistoryDTO history : histories) {
                                    responseContent.put("productHistory", history);
                                    response = new Message(responseContent, MessageType.PRODUCT_HISTORIES);
                                    sendMessage(response);
                                }
                            }
                            break;

                        case GET_PRODUCT_HISTORIES_BY_PRODUCT_ID:
                            productId = (Long) requestContent.get("productId");
                            month = (int) requestContent.get("month");
                            year = (int) requestContent.get("year");

                            historyBLL = new HistoryBLL();
                            histories = historyBLL.findByProductId(productId, month, year);

                            responseContent = new HashMap<>();

                            if (histories.isEmpty()) {
                                responseContent.put("productHistory", null);
                                response = new Message(responseContent, MessageType.PRODUCT_HISTORIES);
                                sendMessage(response);
                            } else {
                                for (HistoryDTO history : histories) {
                                    responseContent.put("productHistory", history);
                                    response = new Message(responseContent, MessageType.PRODUCT_HISTORIES);
                                    sendMessage(response);
                                }
                            }
                            break;

                        case GET_CONFIGURABLE_PRODUCT_HISTORIES:
                            productId = (Long) requestContent.get("productId");
                            Long cpId = (Long) requestContent.get("cpId");
                            month = (int) requestContent.get("month");
                            year = (int) requestContent.get("year");

                            cpHistoryBLL = new ConfigurableProductHistoryBLL();
                            List<ConfigurableProductHistoryDTO> cpHistories = cpHistoryBLL.findByProductIdAndConfigurableProductId(productId, cpId, month, year);

                            responseContent = new HashMap<>();

                            if (cpHistories.isEmpty()) {
                                responseContent.put("configurableProductHistory", null);
                                response = new Message(responseContent, MessageType.CONFIGURABLE_PRODUCT_HISTORIES);
                                sendMessage(response);
                            } else {
                                for (ConfigurableProductHistoryDTO cpHistory : cpHistories) {
                                    responseContent.put("configurableProductHistory", cpHistory);
                                    response = new Message(responseContent, MessageType.CONFIGURABLE_PRODUCT_HISTORIES);
                                    sendMessage(response);
                                }
                            }
                            break;

                        case GET_REVIEWS_BY_PRODUCT_ID:
                            productId = (Long) requestContent.get("productId");

                            reviewBLL = new ReviewBLL();
                            List<ReviewDTO> reviews = reviewBLL.findByProductId(productId);

                            responseContent = new HashMap<>();

                            if (reviews.isEmpty()) {
                                responseContent.put("review", null);
                                response = new Message(responseContent, MessageType.REVIEWS);
                                sendMessage(response);
                            } else {
                                for (ReviewDTO review : reviews) {
                                    responseContent.put("review", review);
                                    response = new Message(responseContent, MessageType.REVIEWS);
                                    sendMessage(response);
                                }
                            }
                            break;

                        default:
                            response = createErrorMessage("Invalid Message!");
                            sendMessage(response);
                            break;
                    }
                }
            }

//                if(response.equals("bye")) {
//                    System.out.println("Server socket closed");
//                    socket.close();
//                    break;
//                }
        } catch (SocketException socketException) {
            socketException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void sendMessage(Object request) throws IOException {
//        Serialize input to a byte array
        byteOutputStream = new ByteArrayOutputStream(bufsize);
        oo = new ObjectOutputStream(new BufferedOutputStream(byteOutputStream));
        oo.writeObject(request);
        oo.flush();
        oo.close();

        byte[] byteData = byteOutputStream.toByteArray();
        System.out.println("Byte Data Length: " + byteData.length);
        dpsend = new DatagramPacket(byteData, byteData.length, dpreceive.getAddress(), dpreceive.getPort());
        socket.send(dpsend);
    }

    private Message receiveMessage() {
        Message response = null;
        try
        {
            byte[] recvBuf = new byte[bufsize];
            dpreceive = new DatagramPacket(recvBuf, recvBuf.length);

            socket.receive(dpreceive);

            byteInputStream = new ByteArrayInputStream(recvBuf);
            oi = new ObjectInputStream(new BufferedInputStream(byteInputStream));

            response = (Message) oi.readObject();

            oi.close();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Message createErrorMessage(String content) {
        Map<String, Object> msgContent = new HashMap<>();
        msgContent.put("error", content);

        return new Message(msgContent, MessageType.ERROR);
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
