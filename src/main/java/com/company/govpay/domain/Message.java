package com.company.govpay.domain;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private String source;
    private String message;
    private Payment payload;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Payment getPayload() {
        return payload;
    }

    public void setPayload(Payment payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Message [message=" + message + ", payload=" + payload + ", source=" + source + "]";
    }
}
