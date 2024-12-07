package com.example.team3.client_chat;

public class Message {

    public static final int TYPE_MESSAGE = 0;  // 일반 메시지 타입
    public static final int TYPE_NOTICE = 1;  // 공지사항 타입
    private String sender;
    private String message;
    private String time;
    private int type;

    public Message(String sender, String message, String time) {
        this.sender = sender;
        this.message = message;
        this.time = time;
    }
    public Message(String sender, String message, String time, int type) {
        this.sender = sender;
        this.message = message;
        this.time = time;
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() { return type; }

    public void setType(int type) { this.type = type;}
}
