package com.ilham.inventoridiecut.data;

public class dataCustomer {
    private String key;

    private String customer;

    public dataCustomer() {
    }

    public dataCustomer(String customer) {
        this.customer = customer;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
