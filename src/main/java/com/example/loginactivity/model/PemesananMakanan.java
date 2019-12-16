package com.example.loginactivity.model;

public class PemesananMakanan {
    String id, makanan;
    int jml;
            Double  harga;
            String tgl,email,status,tanggal;



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PemesananMakanan() {
    }

    public PemesananMakanan(String id, String makanan, int jml, Double harga, String tgl, String email, String status) {
        this.id = id;
        this.makanan = makanan;
        this.jml = jml;
        this.harga = harga;
        this.tgl = tgl;
        this.email = email;
        this.status=status;
        this.tanggal=tanggal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMakanan() {
        return makanan;
    }

    public void setMakanan(String makanan) {
        this.makanan = makanan;
    }

    public int getJml() {
        return jml;
    }

    public void setJml(int jml) {
        this.jml = jml;
    }

    public Double getHarga() {
        return harga;
    }

    public void setHarga(Double harga) {
        this.harga = harga;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
