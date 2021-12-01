package com.tiki_server.main;

import com.tiki_server.dto.ProductDTO;
import com.tiki_server.enums.MessageType;
import com.tiki_server.model.Message;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
    private String hostname = "localhost";
    private int destPort = 1234;
    byte[] recvBuf = new byte[6000];

    private DatagramSocket socket = null;
    private DatagramPacket dpsend = null;
    private DatagramPacket dpreceive = null;
    private InetAddress inetAddress = null;

    private ByteArrayOutputStream byteOutputStream = null;
    private ByteArrayInputStream byteInputStream = null;
    private ObjectOutput oo = null;
    private ObjectInput oi = null;

    public Client() {
    }

    public Client(String hostname, int destPort) {
        this.hostname = hostname;
        this.destPort = destPort;
    }

    public void run() {
        Scanner stdIn;
        try {
            inetAddress = InetAddress.getByName(hostname);	//UnknownHostException
            socket = new DatagramSocket();			//SocketException
            stdIn = new Scanner(System.in);

            String input = "";

            while(true) {
                System.out.print("Client input: ");
                input = stdIn.nextLine();

                if(input.equals("bye")) {
                    System.out.println("Client socket closed");
                    stdIn.close();
                    socket.close();
                    break;
                }

//                Send request to server
                Map<String, Object> request = new HashMap<>();
                request.put("productId", input);

                Message requestMsg = new Message(request, MessageType.GET_PRODUCT);
                sendRequest(requestMsg);

//                Get response from server
                Message responseMsg = receiveResponse();

                if (responseMsg != null) {
                    switch (responseMsg.getMessageType()) {
                        case PRODUCT_INFO:
                            ProductDTO product = (ProductDTO) responseMsg.getContent().get("product");
                            System.out.println("Client receive: " + product);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Object request) throws IOException {
//        Serialize input to a byte array
        byteOutputStream = new ByteArrayOutputStream(recvBuf.length);
        oo = new ObjectOutputStream(new BufferedOutputStream(byteOutputStream));
        oo.writeObject(request);
        oo.flush();
        oo.close();

        byte[] byteData = byteOutputStream.toByteArray();
        dpsend = new DatagramPacket(byteData, byteData.length, inetAddress, destPort);
        socket.send(dpsend);
    }

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

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }
}
