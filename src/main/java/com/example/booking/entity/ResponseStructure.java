package com.example.booking.entity;

import java.time.LocalDateTime;

public class ResponseStructure<T> {

    private int status;
    private String message;
    private T data;
    private String token;
    private LocalDateTime timestamp;

    public ResponseStructure() {
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token=token;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
