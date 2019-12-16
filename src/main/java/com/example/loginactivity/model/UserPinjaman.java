package com.example.loginactivity.model;

public class UserPinjaman {
    String id;
    String email;
    String tglPinjam;
    String jamPinjam;
    String alasan;
    String namarek;
    String norek;
    Long besarPinjam;
    Long lamaPinjam;
    Long angsuran;
    Long sisaPinjam;
    String keterangan;

    public UserPinjaman() {
    }

    public UserPinjaman(String id, String email, String tglPinjam, String jamPinjam, String alasan, String namarek, String norek, Long besarPinjam, Long lamaPinjam, Long angsuran, Long sisaPinjam, String keterangan) {
        this.id = id;
        this.email = email;
        this.tglPinjam = tglPinjam;
        this.jamPinjam = jamPinjam;
        this.alasan = alasan;
        this.namarek = namarek;
        this.norek = norek;
        this.besarPinjam = besarPinjam;
        this.lamaPinjam = lamaPinjam;
        this.angsuran = angsuran;
        this.sisaPinjam = sisaPinjam;
        this.keterangan = keterangan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTglPinjam() {
        return tglPinjam;
    }

    public void setTglPinjam(String tglPinjam) {
        this.tglPinjam = tglPinjam;
    }

    public String getAlasan() {
        return alasan;
    }

    public void setAlasan(String alasan) {
        this.alasan = alasan;
    }

    public String getNamarek() {
        return namarek;
    }

    public void setNamarek(String namarek) {
        this.namarek = namarek;
    }

    public String getNorek() {
        return norek;
    }

    public void setNorek(String norek) {
        this.norek = norek;
    }

    public Long getBesarPinjam() {
        return besarPinjam;
    }

    public void setBesarPinjam(Long besarPinjam) {
        this.besarPinjam = besarPinjam;
    }

    public Long getLamaPinjam() {
        return lamaPinjam;
    }

    public void setLamaPinjam(Long lamaPinjam) {
        this.lamaPinjam = lamaPinjam;
    }

    public Long getAngsuran() {
        return angsuran;
    }

    public void setAngsuran(Long angsuran) {
        this.angsuran = angsuran;
    }

    public Long getSisaPinjam() {
        return sisaPinjam;
    }

    public void setSisaPinjam(Long sisaPinjam) {
        this.sisaPinjam = sisaPinjam;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getJamPinjam() {
        return jamPinjam;
    }

    public void setJamPinjam(String jamPinjam) {
        this.jamPinjam = jamPinjam;
    }
}
