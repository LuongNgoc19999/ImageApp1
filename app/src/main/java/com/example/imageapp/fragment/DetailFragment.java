package com.example.imageapp.fragment;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.imageapp.SQLite.DBManager;
import com.example.imageapp.databinding.FragmentDetailBinding;
import com.example.imageapp.model.ClickDetailFragmentInterface;
import com.example.imageapp.model.Data;
import com.example.imageapp.view.FileActivity;

import org.jetbrains.annotations.NotNull;

import static android.content.Context.ALARM_SERVICE;

public class DetailFragment extends Fragment {
    FragmentDetailBinding binding;
    Data data;
    String name;
    Context context;
    int i;
    boolean isLocal;
    DBManager dbManager;
    ClickDetailFragmentInterface clickDetailFragmentInterface;

    public void setClickDetailFragmentInterface(ClickDetailFragmentInterface clickDetailFragmentInterface) {
        this.clickDetailFragmentInterface = clickDetailFragmentInterface;
    }

    public DetailFragment(Data data, String name, boolean isLocal,int i, Context context) {
        this.data = data;
        this.name = name;
        this.context = context;
        this.i = i;
        this.isLocal = isLocal;
    }
    public DetailFragment(DetailFragment detailFragment) {
        this.data = detailFragment.getData();
        this.name = detailFragment.getName();
        this.context = detailFragment.getContext();
        this.i = detailFragment.getI();
        this.isLocal = detailFragment.isLocal();
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean local) {
        isLocal = local;
    }

    public Data getData() {
        return data;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        dbManager = new DBManager(getContext());
//        binding.btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isLocal){
////                    clickDetailFragmentInterface.clickRemove(data, i);
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setMessage(R.string.dialog_delete_question_file);
//                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dbManager.deleteImageByName(data.getTitle());
//                            dialog.cancel();
//                        }
//                    });
//                    Dialog dialog = builder.create();
//                    dialog.show();
//                }else {
////                    clickDetailFragmentInterface.clickDownload(data, i);
//                    dbManager.insertImage(data, name);
//                }
//            }
//        });
        createView();
        return binding.getRoot();
    }

    private void createView() {
        Glide.with(context).load("http://192.168.1.222:5000/"+data.getImg_url()).error(R.drawable.ic_circle_button).into(binding.imgDetail);
//        if(isLocal){
//            binding.btnSave.setImageResource(R.drawable.ic_baseline_delete_24);
////            binding.btnSave.setVisibility(View.GONE);
////            binding.btnDelete.setVisibility(View.VISIBLE);
//        }else {
//            binding.btnSave.setImageResource(R.drawable.ic_save);
////            binding.btnSave.setVisibility(View.VISIBLE);
////            binding.btnDelete.setVisibility(View.GONE);
//        }
        binding.basePriceDetail.setText(data.getBasePrice());
        binding.dateDetail.setText(data.getDate());
        binding.tittleDetail.setText(data.getTitle());
        binding.totalPriceDetail.setText(data.getTotalPrice());
        binding.yearDetail.setText(data.getYear());
    }
}
