package com.tiki_server.main;

import com.tiki_server.thread.ClientThread;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;

    private Socket socket = null;
    private ServerSocket server = null;

    private Thread serverThread;
    private static ExecutorService pool = Executors.newFixedThreadPool(100);

    private ByteArrayOutputStream byteOutputStream = null;
    private ByteArrayInputStream byteInputStream = null;
    private ObjectOutput oo = null;
    private ObjectInput oi = null;

    public Server(int port) {
        this.port = port;
    }

    public void run() {
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server = new ServerSocket(port);
                    System.out.println("Server started!");

                    while(true) {
                        System.out.println("Waiting for a client ...");
                        socket = server.accept();
                        System.out.println("Client " + server.getInetAddress() + " at port " + server.getLocalPort() + " accepted");
                        ClientThread clientThread = new ClientThread(socket);

                        pool.execute(clientThread);

                    }
                } catch (SocketException socketException) {
                    socketException.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        serverThread.start();
    }

    public void stop() {
        try {
            serverThread.stop();
            socket.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(5001);
        server.run();
    }
}
