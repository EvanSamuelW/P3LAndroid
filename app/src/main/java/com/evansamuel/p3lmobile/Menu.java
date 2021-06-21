package com.evansamuel.p3lmobile;

import java.io.Serializable;

public class Menu implements Serializable {

    private String nama_menu, deskripsi, tipe, gambar, satuan;
    private Integer idMenu,stok;
    private Double harga;

    public Menu(String nama_menu, String deskripsi, String tipe, String gambar, String satuan, Integer idMenu, Double harga,Integer stok) {
        this.nama_menu = nama_menu;
        this.deskripsi = deskripsi;
        this.tipe = tipe;
        this.gambar = gambar;
        this.satuan = satuan;
        this.idMenu = idMenu;
        this.harga = harga;
        this.stok = stok;
    }


    public String getNama_menu() {
        return nama_menu;
    }

    public Integer getStok() {
        return stok;
    }

    public void setStok(Integer stok) {
        this.stok = stok;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getTipe() {
        return tipe;
    }


    public String getGambar() {
        return gambar;
    }


    public String getSatuan() {
        return satuan;
    }


    public Integer getIdMenu() {
        return idMenu;
    }


    public Double getHarga() {
        return harga;
    }

}
