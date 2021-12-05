package com.tiki_server.thread;

import com.tiki_server.model.Message;

import java.io.*;
import java.net.Socket;

public class WriteThread implements Runnable {
    private Socket socket;

    private ByteArrayOutputStream byteOutputStream = null;
    private ObjectOutput out = null;

    private Message message;

    public WriteThread(Socket socket, ObjectOutput out, Message message) throws IOException {
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
        }
    }

//    Send request to server
    public void sendRequest(Object request) throws IOException {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
