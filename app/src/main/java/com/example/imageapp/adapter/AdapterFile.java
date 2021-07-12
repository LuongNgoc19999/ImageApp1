package com.example.imageapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imageapp.R;
import com.example.imageapp.databinding.ItemFileBinding;
import com.example.imageapp.databinding.ItemSearchImageBinding;
import com.example.imageapp.model.Data;
import com.example.imageapp.model.IonClickFile;
import com.example.imageapp.model.IonClickImage;
import com.example.imageapp.model.Shop;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdapterFile extends RecyclerView.Adapter<AdapterFile.ViewHolder> {
    List<Shop> shops;
    Context context;
    IonClickFile ionClickFile;
    AdaperSearchImag adaperSearchImag;

    public AdapterFile(List<Shop> shops, Context context) {
        this.shops = shops;
        this.context = context;
    }

    public void setIonClickFile(IonClickFile ionClickFile) {
        this.ionClickFile = ionClickFile;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemFileBinding binding = ItemFileBinding.inflate(inflater, parent, false);
        AdapterFile.ViewHolder viewHolder = new AdapterFile.ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Shop shop = shops.get(position);
//        Glide.with(context).load("http://192.168.1.222:5000/"+data.getImg_url()).error(R.drawable.ic_circle_button).into(holder.binding.imgSearch);
        adaperSearchImag = new AdaperSearchImag(context, shop.getDataList());
        holder.binding.recy.setAdapter(adaperSearchImag);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, 1 ,RecyclerView.HORIZONTAL, false);
        holder.binding.recy.setLayoutManager(layoutManager);
        holder.binding.txtName.setText(shop.getName());
        holder.binding.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickFile.clickFile(shop, position);
            }
        });
        holder.binding.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickFile.LongClickFile(shop, v, position);
            }
        });

        adaperSearchImag.setIonClickImage(new IonClickImage() {
            @Override
            public void deleteImage(Data data, int i) {

            }
            @Override
            public void clickImage(Data data, int i) {
                ionClickFile.clickFile(shop, position);
            }

            @Override
            public void longClickImage(Data data, View view, int i) {

            }
        });
        holder.binding.layut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickFile.clickFile(shop, position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return shops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemFileBinding binding;
        public ViewHolder(ItemFileBinding binding1) {
            super(binding1.getRoot());
            binding = binding1;
        }
    }
}
