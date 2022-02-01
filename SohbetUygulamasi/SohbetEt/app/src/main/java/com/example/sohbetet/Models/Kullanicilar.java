package com.example.sohbetet.Models;

public class Kullanicilar {

    private String isim;
    private String egitim;
    private String dogumTarih;
    private String hakkimda;
    private String resim;
    private Object state;

    public Kullanicilar() {
    }

    public Kullanicilar(String isim, String egitim, String dogumTarih, String hakkimda, String resim,Object state) {
        this.isim = isim;
        this.egitim = egitim;
        this.dogumTarih = dogumTarih;
        this.hakkimda = hakkimda;
        this.resim = resim;
        this.state = state;
    }

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public String getEgitim() {
        return egitim;
    }

    public void setEgitim(String egitim) {
        this.egitim = egitim;
    }

    public String getDogumTarih() {
        return dogumTarih;
    }

    public void setDogumTarih(String dogumTarih) {
        this.dogumTarih = dogumTarih;
    }

    public String getHakkimda() {
        return hakkimda;
    }

    public void setHakkimda(String hakkimda) {
        this.hakkimda = hakkimda;
    }

    public String getResim() {
        return resim;
    }

    public void setResim(String resim) {
        this.resim = resim;
    }

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Kullanicilar{" +
                "isim='" + isim + '\'' +
                ", egitim='" + egitim + '\'' +
                ", dogumTarih='" + dogumTarih + '\'' +
                ", hakkimda='" + hakkimda + '\'' +
                ", resim='" + resim + '\'' +
                '}';
    }
}
