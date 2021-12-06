package com.tiki_server.model;

import com.tiki_server.enums.MessageType;

import java.io.Serial;
import java.io.Serializable;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = -9069446134464224521L;

    private Object content;
    private MessageType messageType;

    public Message() {
    }

    public Message(Object content, MessageType messageType) {
        this.content = content;
        this.messageType = messageType;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content=" + content +
                ", messageType=" + messageType +
                '}';
    }
}
