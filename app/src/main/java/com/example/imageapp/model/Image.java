package com.example.imageapp.model;

import android.graphics.Bitmap;

public class Image {
    public static final String TYPY_IMAGE = ".jpg";
    String id, name, link;
    Bitmap image;

    public Image(String id, String name, String link, Bitmap image) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.image = image;
    }
    public Image(Image image) {
        this(image.getId(), image.getName(), image.getLink(), image.getImage());
    }

    public static String getTypyImage() {
        return TYPY_IMAGE;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", image=" + image +
                '}';
    }
}
