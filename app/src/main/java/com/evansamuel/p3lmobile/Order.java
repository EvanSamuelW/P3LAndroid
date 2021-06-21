package com.evansamuel.p3lmobile;

import java.io.Serializable;

public class Order implements Serializable {
    private String status,photo,nama_menu;
    private Integer id, id_menu, id_transaksi, jumlah, subtotal;
    private Double harga;


    public Order(String status, String photo, Integer id, Integer id_menu, Integer id_transaksi, Integer jumlah, Integer subtotal, String nama_menu, Double harga) {
        this.status = status;
        this.photo = photo;
        this.id = id;
        this.id_menu = id_menu;
        this.id_transaksi = id_transaksi;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
        this.nama_menu = nama_menu;
        this.harga = harga;
    }


    public Double getHarga() {
        return harga;
    }

    public void setHarga(Double harga) {
        this.harga = harga;
    }

    public String getNama_menu() {
        return nama_menu;
    }

    public void setNama_menu(String nama_menu) {
        this.nama_menu = nama_menu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_menu() {
        return id_menu;
    }

    public void setId_menu(Integer id_menu) {
        this.id_menu = id_menu;
    }

    public Integer getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(Integer id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public Integer getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Integer subtotal) {
        this.subtotal = subtotal;
    }
}
