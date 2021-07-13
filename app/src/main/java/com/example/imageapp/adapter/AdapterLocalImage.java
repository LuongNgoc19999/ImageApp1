package com.example.imageapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.imageapp.R;
import com.example.imageapp.databinding.ItemLoaclImageBinding;
import com.example.imageapp.model.Data;
import com.example.imageapp.model.IonClickImage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterLocalImage extends RecyclerView.Adapter<AdapterLocalImage.ViewHolder> {
    Context context;
    List<Data> dataList;
    IonClickImage ionClickImage;
    ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

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
        Glide.with(context).load("http://192.168.1.222:5000/"+data.getImg_url()).error(R.drawable.ic_circle_button).into(holder.binding.imgLocal);
        holder.binding.nameLocal.setText(data.getTitle());

//        viewBinderHelper.bind(holder.binding.swipeLayout, String.valueOf(data.getId()));

        holder.binding.imgLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickImage.clickImage(data, position);
            }
        });
        holder.binding.nameLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickImage.clickImage(data, position);
            }
        });
        holder.binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickImage.deleteImage(data, position);
            }
        });
        holder.binding.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickImage.deleteImage(data, position);
            }
        });
        holder.binding.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickImage.deleteImage(data, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemLoaclImageBinding binding;
        public ViewHolder(ItemLoaclImageBinding binding1) {
            super(binding1.getRoot());
            binding = binding1;
        }
    }
}
