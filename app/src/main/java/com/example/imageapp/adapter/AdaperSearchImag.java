package com.example.imageapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imageapp.R;
import com.example.imageapp.databinding.ItemSearchImageBinding;
import com.example.imageapp.model.Data;
import com.example.imageapp.model.IonClickImage;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AdaperSearchImag extends RecyclerView.Adapter<AdaperSearchImag.ViewHolder> {
    Context context;
    List<Data> dataList;
    IonClickImage ionClickImage;

    public void setIonClickImage(IonClickImage ionClickImage) {
        this.ionClickImage = ionClickImage;
    }

    public AdaperSearchImag(Context context, List<Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemSearchImageBinding binding = ItemSearchImageBinding.inflate(inflater, parent, false);
        AdaperSearchImag.ViewHolder viewHolder = new AdaperSearchImag.ViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Data data = dataList.get(position);
        Glide.with(context).load("http://192.168.1.222:5000/"+data.getImg_url()).error(R.drawable.ic_circle_button).into(holder.binding.imgSearch);

        holder.binding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ionClickImage.clickImage(data, position);
            }
        });
        holder.binding.imgSearch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ionClickImage.longClickImage(data, v, position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemSearchImageBinding binding;
        public ViewHolder(ItemSearchImageBinding imageBinding) {
            super(imageBinding.getRoot());
            binding = imageBinding;
        }
    }
}
