package com.example.imageapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {
    int id;
    @SerializedName("basePrice")
    String basePrice;
    @SerializedName("date")
    String date;
    @SerializedName("img_url")
    String  img_url;
    @SerializedName("km")
    String  km;
    @SerializedName("repair")
    String  repair;
    @SerializedName("title")
    String  title;
    @SerializedName("totalPrice")
    String  totalPrice;
    @SerializedName("year")
    String  year;
//    @SerializedName("albumId")
//    String  shop;

    public Data(int id, String basePrice, String date, String img_url, String km, String repair, String title, String totalPrice, String year) {
        this.id = id;
        this.basePrice = basePrice;
        this.date = date;
        this.img_url = img_url;
        this.km = km;
        this.repair = repair;
        this.title = title;
        this.totalPrice = totalPrice;
        this.year = year;
//        this.shop = shop;
    }
    public Data() {

    }

    public Data(Data data) {
        this(data.getId(), data.getBasePrice(), data.getDate(), data.getImg_url(), data.getKm(), data.getRepair(), data.getTitle(), data.getTotalPrice(), data.getYear());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getRepair() {
        return repair;
    }

    public void setRepair(String repair) {
        this.repair = repair;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

//    public String getShop() {
//        return shop;
//    }
//
//    public void setShop(String shop) {
//        this.shop = shop;
//    }

    @Override
    public String toString() {
        return "Data{" +
                "basePrice='" + basePrice + '\'' +
                ", date='" + date + '\'' +
                ", img_url='" + img_url + '\'' +
                ", km='" + km + '\'' +
                ", repair='" + repair + '\'' +
                ", title='" + title + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", year='" + year + '\'' +
//                ", shop='" + shop + '\'' +
                '}';
    }
}
