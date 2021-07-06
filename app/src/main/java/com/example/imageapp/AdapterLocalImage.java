package com.example.imageapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imageapp.databinding.ItemLoaclImageBinding;
import com.example.imageapp.model.Data;
import com.example.imageapp.model.Image;
import com.example.imageapp.model.IonClickImage;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterLocalImage extends RecyclerView.Adapter<AdapterLocalImage.ViewHolder> {
    Context context;
    List<Data> dataList;
    IonClickImage ionClickImage;

    public AdapterLocalImage(Context context, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public void setIonClickImage(IonClickImage ionClickImage) {
        this.ionClickImage = ionClickImage;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemLoaclImageBinding binding = ItemLoaclImageBinding.inflate(inflater, parent, false);
        AdapterLocalImage.ViewHolder viewHolder = new AdapterLocalImage.ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Data data = dataList.get(position);
        Picasso.get().load(data.getImg_url()).into(holder.binding.imgLocal);
        holder.binding.nameLocal.setText(data.getTitle());
        holder.binding.imgLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickImage.clickImage(data);
            }
        });
        holder.binding.nameLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickImage.clickImage(data);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemLoaclImageBinding binding;
        public ViewHolder(ItemLoaclImageBinding binding1) {
            super(binding1.getRoot());
            binding = binding1;
        }
    }
}
