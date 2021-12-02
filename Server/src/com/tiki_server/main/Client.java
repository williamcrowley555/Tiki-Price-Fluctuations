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

            Thread readThread = new Thread(new ReadThread(this.socket));
            readThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSocket() {
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

    public static void main(String[] args) {
        Client client = new Client();
        client.run();

        String input = "";
        Scanner stdIn;
        stdIn = new Scanner(System.in);

        System.out.println("CHOOSE AN OPTION");
        System.out.println("1/ Get product with id = 54665");
        System.out.println("2/ Filter products");

        while (true) {
            System.out.print("Client input: ");
            input = stdIn.nextLine();

            try {
                if(input.equals("bye")) {
                    System.out.println("Client socket closed");
                    stdIn.close();
                    client.closeSocket();
                } else if (input.equals("1")) {
                    client.getProduct(54665L);
                } else if (input.equals("2")) {
                    String productName = null;
                    Long categoryId = 1815L;
                    Long brandId = 246045L;
                    Float ratingAverage = 4f;
                    Long minPrice = 0L;
                    Long maxPrice = 300000L;
                    client.filterProducts(productName, categoryId, brandId, ratingAverage, minPrice, maxPrice);
                } else {
                    client.getProduct(-5L);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
