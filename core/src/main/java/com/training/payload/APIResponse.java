package com.training.payload;

public class APIResponse {
    private String message;
    private boolean status;

    public APIResponse(String message, boolean status) {
        this.message = message;
        this.status = status;
    }

    public APIResponse() {
    }

    public String getMessage() {
        return this.message;
    }

    public boolean isStatus() {
        return this.status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String toString() {
        return "APIResponse(message=" + this.getMessage() + ", status=" + this.isStatus() + ")";
    }
}
