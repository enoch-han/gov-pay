package com.company.govpay.domain;

public class Mock {

    /*
        This is a model class to represent a mock data api's data
    */
    private String text;

    public Mock(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }
}
