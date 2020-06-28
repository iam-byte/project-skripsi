package com.ilham.inventoridiecut.data;

public class dataPerbaikan {
    private String key;

    private String nomc;
    private String nodc;
    private String customer;
    private String tglperbaikan;
    private String mesinflexo;
    private String keterangan;
    private String bulan;

    public dataPerbaikan() {
    }

    public dataPerbaikan(String nomc, String nodc, String customer, String tglperbaikan, String mesinflexo, String keterangan, String bulan) {
        this.nomc = nomc;
        this.nodc = nodc;
        this.customer = customer;
        this.tglperbaikan = tglperbaikan;
        this.mesinflexo = mesinflexo;
        this.keterangan = keterangan;
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

    public String getTglperbaikan() {
        return tglperbaikan;
    }

    public void setTglperbaikan(String tglperbaikan) {
        this.tglperbaikan = tglperbaikan;
    }

    public String getMesinflexo() {
        return mesinflexo;
    }

    public void setMesinflexo(String mesinflexo) {
        this.mesinflexo = mesinflexo;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getBulan() {
        return bulan;
    }

    public void setBulan(String bulan) {
        this.bulan = bulan;
    }
}
