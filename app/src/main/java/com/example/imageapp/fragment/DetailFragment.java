package com.example.imageapp.fragment;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.imageapp.R;
import com.example.imageapp.databinding.FragmentDetailBinding;
import com.example.imageapp.model.Data;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.ALARM_SERVICE;

public class DetailFragment extends Fragment {
    FragmentDetailBinding binding;
    Data data;
    Context context;

    public DetailFragment(Data data, Context context) {
        this.data = data;
        this.context = context;
    }
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        createView();
        return binding.getRoot();
    }

    private void createView() {
        Glide.with(context).load("http://192.168.1.222:5000/"+data.getImg_url()).error(R.drawable.ic_circle_button).into(binding.imgDetail);
        binding.basePriceDetail.setText(data.getBasePrice());
        binding.dateDetail.setText(data.getDate());
        binding.tittleDetail.setText(data.getTitle());
        binding.totalPriceDetail.setText(data.getTotalPrice());
        binding.yearDetail.setText(data.getYear());
    }
}
