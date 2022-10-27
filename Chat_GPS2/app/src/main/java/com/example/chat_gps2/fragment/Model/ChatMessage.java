package com.example.chat_gps2.fragment.Model;

public class ChatMessage {

    private String messageText;
    private String messageUser;
    private String imgURL;


    public ChatMessage(String messageText, String messageUser, String imgURL) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.imgURL = imgURL;


    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }
    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }
}
