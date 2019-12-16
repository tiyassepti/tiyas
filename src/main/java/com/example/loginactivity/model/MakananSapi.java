package com.example.loginactivity.model;

public class MakananSapi {
    public MakananSapi(String id, String nama, Double harga) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
    }
    public MakananSapi() {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Double getHarga() {
        return harga;
    }

    public void setHarga(Double harga) {
        this.harga = harga;
    }

    String id, nama;
    Double harga;
}
