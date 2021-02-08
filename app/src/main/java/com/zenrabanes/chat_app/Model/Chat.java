package com.zenrabanes.chat_app.Model;

public class Chat {
    private String sender;
    private String message;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Chat(String sender, String message, String id) {
        this.sender = sender;
        this.message = message;
        this.id = id;
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


}
