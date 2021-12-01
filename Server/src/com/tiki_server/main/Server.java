package com.tiki_server.main;

import com.tiki_server.bll.IHistoryBLL;
import com.tiki_server.bll.IProductBLL;
import com.tiki_server.bll.impl.HistoryBLL;
import com.tiki_server.bll.impl.ProductBLL;
import com.tiki_server.dto.HistoryDTO;
import com.tiki_server.dto.ProductDTO;
import com.tiki_server.enums.MessageType;
import com.tiki_server.model.Message;
import com.tiki_server.util.InputValidatorUtil;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private int port = 1234;
    private int bufsize = 6000;

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
                    Message response = null;

                    switch (clientRequest.getMessageType()) {
                        case GET_PRODUCT:
                            String productId = (String) clientRequest.getContent().get("productId");
                            if (InputValidatorUtil.isLong(productId).isEmpty()) {
                                IProductBLL productBLL = new ProductBLL();
                                ProductDTO product = productBLL.findById(Long.valueOf(productId));

                                Map<String, Object> content = new HashMap<>();
                                content.put("product", product);

                                response = new Message(content, MessageType.PRODUCT_INFO);
                            }
                            break;

                        default:
                            Map<String, Object> content = new HashMap<>();
                            content.put("error", "Invalid Message!");

                            response = new Message(content, MessageType.ERROR);
                            break;
                    }

                    sendMessage(response);
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
        System.out.println(byteData.length);
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

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}
