package com.ergnologi.firebaseservicenotification;

public class ChatModels {
    private String username;
    private String date;
    private String chat;

    public ChatModels() {

    }

    public ChatModels(String username, String date, String chat) {
        this.chat = chat;
        this.username = username;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }
}
