package com.ilham.inventoridiecut.data;

public class dataUserPPIC {
    private String key;

    private String nama;
    private String password;
    private String sap;
    private String jenis_kelamin;
    private String agama;
    private String no_hp;
    private String bagian;
    private String sebagai;
    private String alamat;


    public dataUserPPIC(String nama, String password, String sap, String jenis_kelamin, String agama, String no_hp, String bagian, String sebagai, String alamat) {
        this.nama = nama;
        this.password = password;
        this.sap = sap;
        this.jenis_kelamin = jenis_kelamin;
        this.agama = agama;
        this.no_hp = no_hp;
        this.bagian = bagian;
        this.sebagai = sebagai;
        this.alamat = alamat;
    }

    public dataUserPPIC() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSap() {
        return sap;
    }

    public void setSap(String sap) {
        this.sap = sap;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public String getAgama() {
        return agama;
    }

    public void setAgama(String agama) {
        this.agama = agama;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getBagian() {
        return bagian;
    }

    public void setBagian(String bagian) {
        this.bagian = bagian;
    }

    public String getSebagai() {
        return sebagai;
    }

    public void setSebagai(String sebagai) {
        this.sebagai = sebagai;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
