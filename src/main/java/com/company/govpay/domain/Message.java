package com.company.govpay.domain;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private String source;
    private String message;
    private Payment payload;
    private int completedPercentage = 0;

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

    public int getCompletionPercentage() {
        return completedPercentage;
    }

    public void setCompletionPercentage(int percentage) {
        this.completedPercentage = percentage;
    }

    public void completeOneQueueCycle() {
        this.completedPercentage = this.completedPercentage + 25;
    }

    @Override
    public String toString() {
        return (
            "Message [message=" +
            message +
            ", payload=" +
            payload +
            ", source=" +
            source +
            ", completedPercentage=" +
            completedPercentage +
            "]"
        );
    }
}
