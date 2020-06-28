package com.ilham.inventoridiecut.data;

public class dataPengambilan {

    private String key;

    private String nomc;
    private String nodc;
    private String customer;
    private String tglkeluar;
    private String mesinflexo;
    private String namaoperator;
    private String nik;
    private String bulan;

    public dataPengambilan() {
    }

    public dataPengambilan(String nomc, String nodc, String customer, String tglkeluar, String mesinflexo, String namaoperator, String nik, String bulan) {
        this.nomc = nomc;
        this.nodc = nodc;
        this.customer = customer;
        this.tglkeluar = tglkeluar;
        this.mesinflexo = mesinflexo;
        this.namaoperator = namaoperator;
        this.nik = nik;
        this.bulan = bulan;
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

    public String getTglkeluar() {
        return tglkeluar;
    }

    public void setTglkeluar(String tglkeluar) {
        this.tglkeluar = tglkeluar;
    }

    public String getMesinflexo() {
        return mesinflexo;
    }

    public void setMesinflexo(String mesinflexo) {
        this.mesinflexo = mesinflexo;
    }

    public String getNamaoperator() {
        return namaoperator;
    }

    public void setNamaoperator(String namaoperator) {
        this.namaoperator = namaoperator;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }
}
