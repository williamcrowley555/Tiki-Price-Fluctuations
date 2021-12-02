package com.tiki_server.thread;

import com.tiki_server.model.Message;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class WriteThread implements Runnable {
    private final byte[] recvBuf = new byte[100000];

    private int destPort;
    private InetAddress inetAddress = null;

    private DatagramSocket socket = null;
    private DatagramPacket dpsend = null;

    private ByteArrayOutputStream byteOutputStream = null;
    private ObjectOutput oo = null;

    private Message message;

    public WriteThread(int destPort, InetAddress inetAddress, DatagramSocket socket, Message message) {
        this.destPort = destPort;
        this.inetAddress = inetAddress;
        this.socket = socket;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            sendRequest(this.message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    Send request to server
    public void sendRequest(Object request) throws IOException {
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
}
