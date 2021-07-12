package com.example.imageapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Shop implements Serializable {
    int id;
    private String name;
    List<Data> dataList;

    public Shop(int id, String name, List<Data> dataList) {
        this.id = id;
        this.name = name;
        this.dataList = dataList;
    }
    public Shop(Shop d) {
        this(d.getId(), d.getName(), d.getDataList());
    }

    public Shop() {
        this(0, "Defaul", new ArrayList<Data>());
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
