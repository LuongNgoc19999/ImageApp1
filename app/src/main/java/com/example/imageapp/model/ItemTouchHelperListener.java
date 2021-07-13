package com.example.imageapp.model;

import androidx.recyclerview.widget.RecyclerView;

public interface  ItemTouchHelperListener {
    void onSwipe(RecyclerView.ViewHolder viewHolder);
}