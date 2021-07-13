package com.example.imageapp.model;

import android.view.View;

import com.example.imageapp.R;

public interface IonClickImage {
    void clickImage(Data data, int i);
    void longClickImage(Data data, View view, int i);
    void deleteImage(Data data, int i);
}
