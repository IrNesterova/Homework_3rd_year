package ru.itis.models;

public class MessageData {
    public String postTitle;
    public String message;

    public MessageData(String postTitle, String message) {
        this.postTitle = postTitle;
        this.message = message;
    }


    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
