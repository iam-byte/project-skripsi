package com.ilham.inventoridiecut.data;

public class dataPembuatanDieCut {
    private String key;

    private String nomc;
    private String nodc;
    private String customer;
    private String tglbuat;
    private String tglselesai;
    private String lokasi;
    private String mold;
    private String papan;
    private String pisau;
    private String creasing;

    public dataPembuatanDieCut() {
    }

    public dataPembuatanDieCut(String nomc, String nodc, String customer, String tglbuat, String tglselesai, String lokasi, String mold, String papan, String pisau, String creasing) {
        this.nomc = nomc;
        this.nodc = nodc;
        this.customer = customer;
        this.tglbuat = tglbuat;
        this.tglselesai = tglselesai;
        this.lokasi = lokasi;
        this.mold = mold;
        this.papan = papan;
        this.pisau = pisau;
        this.creasing = creasing;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNomc() {
        return nomc;
    }

    public void setNomc(String nomc) {
        this.nomc = nomc;
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

    public String getTglbuat() {
        return tglbuat;
    }

    public void setTglbuat(String tglbuat) {
        this.tglbuat = tglbuat;
    }

    public String getTglselesai() {
        return tglselesai;
    }

    public void setTglselesai(String tglselesai) {
        this.tglselesai = tglselesai;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getMold() {
        return mold;
    }

    public void setMold(String mold) {
        this.mold = mold;
    }

    public String getPapan() {
        return papan;
    }

    public void setPapan(String papan) {
        this.papan = papan;
    }

    public String getPisau() {
        return pisau;
    }

    public void setPisau(String pisau) {
        this.pisau = pisau;
    }

    public String getCreasing() {
        return creasing;
    }

    public void setCreasing(String creasing) {
        this.creasing = creasing;
    }
}
