package com.company.govpay.domain;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private String source;
    private String message;
    private Payment payload;
    private CheckType check = CheckType.NONE;
    private String checkURL;
    private String nextQueue;
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

    public CheckType getCheck() {
        return this.check;
    }

    public void setCheck(CheckType check) {
        this.check = check;
    }

    public String getNextQueue() {
        return this.nextQueue;
    }

    public void setNextQueue(String nextQueue) {
        this.nextQueue = nextQueue;
    }

    public String getCheckURL() {
        return this.checkURL;
    }

    public void setCheckURL(String checkURL) {
        this.checkURL = checkURL;
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
            ", check=" +
            check +
            ", checkURL=" +
            checkURL +
            ", nextQueue=" +
            nextQueue +
            ", completedPercentage=" +
            completedPercentage +
            "]"
        );
    }
}