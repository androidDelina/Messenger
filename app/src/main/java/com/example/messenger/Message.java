package com.example.messenger;

public class Message {
    private String text;
    private String senderId;
    private String receiverId;

    public Message(String text, String senderId, String receiverId) {
        this.text = text;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public Message() {
    }

    public String getText() {
        return text;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                '}';
    }
}
