package com.example.loginactivity.model;

public class Pinjaman {
    String id;
    String idPeternak;
    String nama;
    String tglPinjam;
    Integer besarPinjam;
    Integer lamaPinjam;

    public Pinjaman() {
    }

    public Pinjaman(String id, String idPeternak, String nama, String tglPinjam, Integer besarPinjam, Integer lamaPinjam) {
        this.id = id;
        this.idPeternak = idPeternak;
        this.nama = nama;
        this.tglPinjam = tglPinjam;
        this.besarPinjam = besarPinjam;
        this.lamaPinjam = lamaPinjam;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdPeternak() {
        return idPeternak;
    }

    public void setIdPeternak(String idPeternak) {
        this.idPeternak = idPeternak;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTglPinjam() {
        return tglPinjam;
    }

    public void setTglPinjam(String tglPinjam) {
        this.tglPinjam = tglPinjam;
    }

    public Integer getBesarPinjam() {
        return besarPinjam;
    }

    public void setBesarPinjam(Integer besarPinjam) {
        this.besarPinjam = besarPinjam;
    }

    public Integer getLamaPinjam() {
        return lamaPinjam;
    }

    public void setLamaPinjam(Integer lamaPinjam) {
        this.lamaPinjam = lamaPinjam;
    }
}
