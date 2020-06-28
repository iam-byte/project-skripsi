package com.ilham.inventoridiecut.data;

public class dataPenarikan {

    private String key;

    private String nomc;
    private String nodc;
    private String customer;
    private String tglmasuk;
    private String mesinflexo;
    private String nama;
    private String nik;
    private String bulan;

    public dataPenarikan() {
    }

    public dataPenarikan(String nomc, String nodc, String customer, String tglmasuk, String mesinflexo, String nama, String nik, String bulan) {
        this.nomc = nomc;
        this.nodc = nodc;
        this.customer = customer;
        this.tglmasuk = tglmasuk;
        this.mesinflexo = mesinflexo;
        this.nama = nama;
        this.nik = nik;
        this.bulan = bulan;
    }

    public String getNomc() {
        return nomc;
    }

    public void setNomc(String nomc) {
        this.nomc = nomc;
    }

    public String getBulan() {
        return bulan;
    }

    public void setBulan(String bulan) {
        this.bulan = bulan;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNodc() {
        return nodc;
    }

    public void setNodc(String nodc) {
        this.nodc = nodc;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getTglmasuk() {
        return tglmasuk;
    }

    public void setTglmasuk(String tglmasuk) {
        this.tglmasuk = tglmasuk;
    }

    public String getMesinflexo() {
        return mesinflexo;
    }

    public void setMesinflexo(String mesinflexo) {
        this.mesinflexo = mesinflexo;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }
}
