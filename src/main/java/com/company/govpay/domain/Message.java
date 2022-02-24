package com.company.govpay.domain;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private String source;
    private String customMessage;
    private Payment payload;
    private CheckType check = CheckType.NONE;
    private String checkURL;
    private String nextQueue;
    private int completionPercentage = 0;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMessage() {
        return customMessage;
    }

    public void setMessage(String message) {
        this.customMessage = message;
    }

    public Payment getPayload() {
        return payload;
    }

    public void setPayload(Payment payload) {
        this.payload = payload;
    }

    public int getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(int percentage) {
        this.completionPercentage = percentage;
    }

    public void completeOneQueueCycle() {
        this.completionPercentage = this.completionPercentage + 25;
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
            customMessage +
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
            ", completionPercentage=" +
            completionPercentage +
            "]"
        );
    }
}
