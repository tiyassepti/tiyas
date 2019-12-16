package com.example.loginactivity.model;

public class PeternakRegister {
    private String id;
    private String email;
    private String nama;
    private String password;
    private String nohp;
    private String ktp;
    private String kelurahan;
    private String gambar;
    private String userType;

    public PeternakRegister() {
    }

    public PeternakRegister(String id, String email, String nama, String password, String nohp, String ktp, String kelurahan, String gambar, String userType) {
        this.id = id;
        this.email = email;
        this.nama = nama;
        this.password = password;
        this.nohp = nohp;
        this.ktp = ktp;
        this.kelurahan = kelurahan;
        this.gambar = gambar;
        this.userType = userType;
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

    public String getNohp() {
        return nohp;
    }

    public void setNohp(String nohp) {
        this.nohp = nohp;
    }

    public String getKtp() {
        return ktp;
    }

    public void setKtp(String ktp) {
        this.ktp = ktp;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
