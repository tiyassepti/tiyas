package com.example.loginactivity.model;

public class Angsuran {
    String kodeAngsuran;
    String kodePinjaman;
    String email;
    Long angsuranKe;
    Long jumlah;
    String jatuhTempo;
    String status;
    String bukti;

    public Angsuran() {
    }

    public Angsuran(String kodeAngsuran, String kodePinjaman, String email, Long angsuranKe, Long jumlah, String jatuhTempo, String status, String bukti) {
        this.kodeAngsuran = kodeAngsuran;
        this.kodePinjaman = kodePinjaman;
        this.email = email;
        this.angsuranKe = angsuranKe;
        this.jumlah = jumlah;
        this.jatuhTempo = jatuhTempo;
        this.status = status;
        this.bukti = bukti;
    }

    public String getKodeAngsuran() {
        return kodeAngsuran;
    }

    public void setKodeAngsuran(String kodeAngsuran) {
        this.kodeAngsuran = kodeAngsuran;
    }

    public String getKodePinjaman() {
        return kodePinjaman;
    }

    public void setKodePinjaman(String kodePinjaman) {
        this.kodePinjaman = kodePinjaman;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getAngsuranKe() {
        return angsuranKe;
    }

    public void setAngsuranKe(Long angsuranKe) {
        this.angsuranKe = angsuranKe;
    }

    public Long getJumlah() {
        return jumlah;
    }

    public void setJumlah(Long jumlah) {
        this.jumlah = jumlah;
    }

    public String getJatuhTempo() {
        return jatuhTempo;
    }

    public void setJatuhTempo(String jatuhTempo) {
        this.jatuhTempo = jatuhTempo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBukti() {
        return bukti;
    }

    public void setBukti(String bukti) {
        this.bukti = bukti;
    }
}
