package com.ilham.inventoridiecut.data;

public class dataLogin {
    private String username;
    private String sap;
    private String password;
    private String sebagai;
    private String image;

    public dataLogin() {
    }


    public dataLogin(String username, String sap, String password, String sebagai, String image) {
        this.username = username;
        this.sap = sap;
        this.password = password;
        this.sebagai = sebagai;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSap() {
        return sap;
    }

    public void setSap(String sap) {
        this.sap = sap;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSebagai() {
        return sebagai;
    }

    public void setSebagai(String sebagai) {
        this.sebagai = sebagai;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
