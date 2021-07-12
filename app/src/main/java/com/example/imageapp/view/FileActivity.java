package com.example.imageapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imageapp.MainActivity;
import com.example.imageapp.R;
import com.example.imageapp.SQLite.DBManager;
import com.example.imageapp.adapter.AdapterFile;
import com.example.imageapp.adapter.AdapterLocalImage;
import com.example.imageapp.databinding.ActivityFileBinding;
import com.example.imageapp.model.Data;
import com.example.imageapp.model.IonClickFile;
import com.example.imageapp.model.IonClickImage;
import com.example.imageapp.model.Shop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileActivity extends AppCompatActivity {
    public static final boolean ALL_FILE = true;
    public static final boolean DETAIL_FILE = false;


    public boolean status_file = ALL_FILE;
    DBManager dbManager;
    ActivityFileBinding binding;
    List<Shop> shops = new ArrayList<>();
    String first_shop_name = "Default";
    String shop_name = "Default";
    AdapterFile adapterFile;
    List<Data> dataList = new ArrayList<>();
    AdapterLocalImage adapterLocalImage;
    RecyclerView.LayoutManager imageLayoutManager = new GridLayoutManager(getBaseContext(), 1, RecyclerView.VERTICAL, false);
    RecyclerView.LayoutManager fileLayoutManager = new GridLayoutManager(getBaseContext(), 1, RecyclerView.VERTICAL, false);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_file);
        dbManager = new DBManager(getBaseContext());
//        getintent();
//        initListImage("Default");
        initListFile();
        Click();

    }

    @Override
    protected void onResume() {
        super.onResume();
        upgradrListFile();
    }

    private void upgradrListFile() {
        shops.clear();
        shops.addAll(dbManager.getAllShop());
        shops.remove(0);
        if(shops.size()>0){
            for (int i=0;i<shops.size();i++){
                shops.get(i).setDataList(dbManager.getAllImage(shops.get(i).getName()));
            }
        }
        adapterFile.notifyDataSetChanged();
    }
    //    private void getintent(){
//        Intent intent = getIntent();
//        shop_name = intent.getStringExtra("name");
//        first_shop_name = shop_name;
//        Log.d("File", shop_name);
//    }

    private void Click() {
        binding.btnAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status_file){
                    ShowDialogCreateFile(new Shop(), false);
                }else {
//                    Intent intent=new Intent();
//                    intent.putExtra("name",shop_name);
//                    setResult(1999,intent);
//                    status_file = ALL_FILE;
//                    finish();
                }
            }
        });
    }

    private void ShowDialogCreateFile(Shop shop, boolean isChange) {
        Dialog dialog = new Dialog(FileActivity.this);
        dialog.setContentView(R.layout.dialog_add_file);
        EditText txtName = dialog.findViewById(R.id.dialog_txt_new_name);
        TextView btnCancel = dialog.findViewById(R.id.dialog_btnHuy);
        TextView btnOK = dialog.findViewById(R.id.dialog_btnOk);
        if(isChange){
            txtName.setText(shop.getName());
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtName.getText().toString().equalsIgnoreCase("")){
                    Toast.makeText(getBaseContext(), "File name is null!", Toast.LENGTH_SHORT).show();
                }else {
                    if(isChange){
                        if(dbManager.ifExistsShop(txtName.getText().toString())){
                            Toast.makeText(getBaseContext(), "This name already exists!", Toast.LENGTH_SHORT).show();
                        }else {
                            dbManager.deleteImageByShop(shop.getName());
                            shop.setName(txtName.getText().toString());
                            dbManager.updateShop(shop);
                            for (Data d: shop.getDataList()){
                                dbManager.insertImage(d, shop.getName());
                            }
                            updateGroup();
                            adapterFile.notifyDataSetChanged();
                            dialog.cancel();
                        }
                    }else {
                        if(dbManager.ifExistsShop(txtName.getText().toString())){
                            Toast.makeText(getBaseContext(), "This name already exists!", Toast.LENGTH_SHORT).show();
                        }else {
                            shop_name = txtName.getText().toString();
                            Shop shop = new Shop(0,shop_name, new ArrayList<>());
                            dbManager.insertShop(shop);
//                        upgradeImageLocal(shop_name);
                            updateGroup();

                            adapterFile.notifyDataSetChanged();
                            dialog.cancel();
                        }
                    }

                }
            }
        });
        dialog.create();
        dialog.show();
    }
    private void updateGroup(){
        shops.clear();
        shops.addAll(dbManager.getAllShop());
        shops.remove(0);
        if(shops.size()>0){
            for (int i=0;i<shops.size();i++){
                shops.get(i).setDataList(dbManager.getAllImage(shops.get(i).getName()));
            }
        }
    }
//
//    private void initListImage(String name_file){
//        dataList.clear();
//        dataList.addAll(dbManager.getAllImage(name_file));
//        adapterLocalImage = new AdapterLocalImage(getBaseContext(), dataList);
//        binding.lv.setLayoutManager(imageLayoutManager);
////        binding.lv.setAdapter(adapterLocalImage);
//        adapterLocalImage.setIonClickImage(new IonClickImage() {
//            @Override
//            public void clickImage(Data data, int i) {
//
//            }
//
//            @Override
//            public void longClickImage(Data data, View view, int i) {
//
//            }
//        });
//    }
    private void upgradeImageLocal(String name_file){
        status_file = DETAIL_FILE;
        CheckStatus();
        dataList.clear();
        dataList.addAll(dbManager.getAllImage(name_file));
        adapterLocalImage.notifyDataSetChanged();
        binding.lv.setLayoutManager(imageLayoutManager);

    }

    private void initListFile() {
        updateGroup();
        adapterFile = new AdapterFile(shops, getBaseContext());
        binding.lv.setAdapter(adapterFile);
        binding.lv.setLayoutManager(fileLayoutManager);
        adapterFile.setIonClickFile(new IonClickFile() {
            @Override
            public void clickFile(Shop shop, int i) {
//                shop_name = shop.getName();
//                upgradeImageLocal(shop_name);
                Intent intent = new Intent(getBaseContext(), GroupCameraActivity.class);
                intent.putExtra("group", (Serializable) shop);
                startActivity(intent);
            }

            @Override
            public void LongClickFile(Shop shop, View view, int i) {
                PopupMenu popup = new PopupMenu(FileActivity.this, view);
                popup.inflate(R.menu.menu_item_file);

                Menu menu = popup.getMenu();
                // com.android.internal.view.menu.MenuBuilder
//                Log.i(LOG_TAG, "Menu class: " + menu.getClass().getName());

                // Register Menu Item Click event.
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_delete_file:
                                AlertDialog.Builder builder = new AlertDialog.Builder(FileActivity.this);
                                builder.setMessage(R.string.dialog_delete_question_file);
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dbManager.deleteImageByShop(shop.getName());
                                        dbManager.deleteShop(shop.getName());
                                        updateGroup();
                                        adapterFile.notifyDataSetChanged();
                                        dialog.cancel();
                                    }
                                });
                                Dialog dialog = builder.create();
                                dialog.show();
                                break;
                            case R.id.menu_change_name_file:
                                ShowDialogCreateFile(shop, true);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });

                // Show the PopupMenu.
                popup.show();
            }
        });
    }

    private void CheckStatus(){
        if(status_file){
            binding.lv.setAdapter(adapterFile);
            binding.lv.setLayoutManager(fileLayoutManager);
        }else {
            binding.lv.setAdapter(adapterLocalImage);
            binding.lv.setLayoutManager(imageLayoutManager);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
//        if(status_file){
////            Intent intent=new Intent();
////            intent.putExtra("name",first_shop_name);
////            setResult(1999,intent);
////            status_file = ALL_FILE;
//            finish();
//        }else {
//            status_file = ALL_FILE;
//            CheckStatus();
//        }

    }
}