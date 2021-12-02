package com.tiki_server.main;

import com.tiki_server.enums.MessageType;
import com.tiki_server.model.Message;
import com.tiki_server.thread.ReadThread;
import com.tiki_server.thread.WriteThread;

import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
    private String hostname = "localhost";
    private int destPort = 1234;
    private InetAddress inetAddress = null;

    private DatagramSocket socket = null;

    Thread readThread;

    public Client() {
    }

    public Client(String hostname, int destPort) {
        this.hostname = hostname;
        this.destPort = destPort;
    }

    public void run() {
        try {
            inetAddress = InetAddress.getByName(hostname);	//UnknownHostException
            socket = new DatagramSocket();			//SocketException

            readThread = new Thread(new ReadThread(this.socket));
            readThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSocket() {
        this.socket.disconnect();
        this.socket.close();
    }

    public void sendMessage(Message message) {
        Thread writeThread = new Thread(new WriteThread(this.destPort, this.inetAddress, this.socket, message));
        writeThread.start();
    }

    public void getProduct(Long productId) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("productId", productId);

        Message requestMsg = new Message(request, MessageType.GET_PRODUCT);
        sendMessage(requestMsg);
    }

    public void filterProducts(String productName, Long categoryId, Long brandId, float ratingAverage, Long minPrice, Long maxPrice) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("productName", productName);
        request.put("categoryId", categoryId);
        request.put("brandId", brandId);
        request.put("ratingAverage", ratingAverage);
        request.put("minPrice", minPrice);
        request.put("maxPrice", maxPrice);

        Message requestMsg = new Message(request, MessageType.FILTER_PRODUCTS);
        sendMessage(requestMsg);
    }

    public void getConfigurableProducts(Long productId) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("productId", productId);

        Message requestMsg = new Message(request, MessageType.GET_CONFIGURABLE_PRODUCTS);
        sendMessage(requestMsg);
    }

    public void getProductHistories(String url, int month, int year) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("url", url);
        request.put("month", month);
        request.put("year", year);

        Message requestMsg = new Message(request, MessageType.GET_PRODUCT_HISTORIES_BY_URL);
        sendMessage(requestMsg);
    }

    public void getProductHistories(Long productId, int month, int year) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("productId", productId);
        request.put("month", month);
        request.put("year", year);

        Message requestMsg = new Message(request, MessageType.GET_PRODUCT_HISTORIES_BY_PRODUCT_ID);
        sendMessage(requestMsg);
    }

    public void getConfigurableProductHistories(Long productId, Long cpId, int month, int year) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("productId", productId);
        request.put("cpId", cpId);
        request.put("month", month);
        request.put("year", year);

        Message requestMsg = new Message(request, MessageType.GET_CONFIGURABLE_PRODUCT_HISTORIES);
        sendMessage(requestMsg);
    }

    public void getReviews(Long productId) throws IOException {
        Map<String, Object> request = new HashMap<>();
        request.put("productId", productId);

        Message requestMsg = new Message(request, MessageType.GET_REVIEWS_BY_PRODUCT_ID);
        sendMessage(requestMsg);
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();

        String input = "";
        Scanner stdIn;
        stdIn = new Scanner(System.in);

        System.out.println("CHOOSE AN OPTION");
        System.out.println("1/ Get product with id = 249953");
        System.out.println("2/ Filter products");
        System.out.println("3/ Get configurable products with product id = 249953");
        System.out.println("4/ Get product histories by URL in 11/2021");
        System.out.println("5/ Get product histories with product id = 249953 in 11/2021");
        System.out.println("6/ Get configurable product histories with product id = 249953 & cpId = 511074 in 11/2021");
        System.out.println("7/ Get reviews with product id = 249953");

        while (true) {
            System.out.print("Client input: ");
            input = stdIn.nextLine();

            try {
                if(input.equals("bye")) {
                    System.out.println("Client socket closed");
                    stdIn.close();
                    client.closeSocket();
                    break;
                } else if (input.equals("1")) {
                    Long productId = 249953L;
                    client.getProduct(productId);
                } else if (input.equals("2")) {
                    String productName = null;
                    Long categoryId = 1815L;
                    Long brandId = 246045L;
                    Float ratingAverage = 4f;
                    Long minPrice = 0L;
                    Long maxPrice = 300000L;
                    client.filterProducts(productName, categoryId, brandId, ratingAverage, minPrice, maxPrice);
                } else if (input.equals("3")) {
                    client.getConfigurableProducts(249953L);
                } else if (input.equals("4")) {
                    String url = "https://tiki.vn/day-cap-sac-micro-usb-anker-powerline-0-9m-a8132-hang-chinh-hang-p249953.html?spid=249956";
                    int month = 11;
                    int year = 2021;
                    client.getProductHistories(url, month, year);
                } else if (input.equals("5")) {
                    Long productId = 249953L;
                    int month = 11;
                    int year = 2021;
                    client.getProductHistories(productId, month, year);
                } else if (input.equals("6")) {
                    Long productId = 249953L;
                    Long cpId = 511074L;
                    int month = 11;
                    int year = 2021;
                    client.getConfigurableProductHistories(productId, cpId, month, year);
                } else if (input.equals("7")) {
                    Long productId = 249953L;
                    client.getReviews(productId);
                } else {
                    client.getProduct(-5L);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
