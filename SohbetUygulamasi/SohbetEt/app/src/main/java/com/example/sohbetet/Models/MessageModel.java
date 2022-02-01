package com.example.sohbetet.Models;

public class MessageModel {

    private String userId ;
    private String otherId;
    private String text;
    private String time;
    private String type;
    private String from;
    private Boolean seen;

    public MessageModel() {
    }

    public MessageModel(String from , Boolean seen, String text, String time, String type ) {
        this.from = from;
        this.seen = seen;
        this.text = text;
        this.time = time;
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "userId='" + userId + '\'' +
                ", otherId='" + otherId + '\'' +
                ", text='" + text + '\'' +
                ", time='" + time + '\'' +
                ", type='" + type + '\'' +
                ", from='" + from + '\'' +
                ", seen=" + seen +
                '}';
    }
}
