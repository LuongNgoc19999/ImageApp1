package com.example.imageapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imageapp.SQLite.DBManager;
import com.example.imageapp.adapter.AdaperSearchImag;
import com.example.imageapp.adapter.AdapterPager;
import com.example.imageapp.databinding.ActivityMainBinding;
import com.example.imageapp.fragment.DetailFragment;
import com.example.imageapp.model.Data;
import com.example.imageapp.model.GenerPrecenter;
import com.example.imageapp.model.Image;
import com.example.imageapp.model.IonClickImage;
import com.example.imageapp.model.Shop;
import com.google.gson.Gson;
import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.PictureResult;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final boolean CAPTURE = true;
    public static final boolean DETAIL = false;
    private boolean isCapture = CAPTURE;
    public static final String TAG = "Main";
    public static final float zoom05 = 0.5f;
    public static final float zoom10 = 1f;
    public static final float zoom15 = 1.5f;
    public static final float zoom20 = 2.0f;
    private float zoomState = zoom10;
    ActivityMainBinding binding;
    List<Data> imageSearch = new ArrayList<>();
    AdaperSearchImag adapterSearch;
    FileOutputStream outputStream;
    List<Fragment> fragmentList = new ArrayList<>();
    AdapterPager adapterPager;
    DetailFragment detailFragment;
    DBManager dbManager;

    public void initViewPager(int i){

        adapterPager = new AdapterPager(getSupportFragmentManager());
        for(Data data: imageSearch){
            detailFragment = new DetailFragment(data, getBaseContext());
            adapterPager.AddFragment(detailFragment);
        }
        binding.viewPager.setAdapter(adapterPager);
        binding.viewPager.setCurrentItem(i);
        isCapture = DETAIL;
        CheckCapture();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        dbManager = new DBManager(getBaseContext());
        setUpCamera();
        ckick();
        CheckCapture();
        capture();
        initSearchRecyclerView();

    }

    private void insertShop() {
        List<Shop> shops = new ArrayList<>();
        shops.add(new Shop(1,"Shop 1"));
        shops.add(new Shop(2,"Shop 2"));
        shops.add(new Shop(3,"Shop 3"));
        shops.add(new Shop(4,"Shop 4"));
        for (Shop s: shops){
            dbManager.insertShop(s);
        }
        Log.d("mop", "size: "+String.valueOf(dbManager.getAllShop().size()));
    }

    private void CheckCapture() {
        if (isCapture) {
            binding.layoutCapure.setVisibility(View.VISIBLE);
            binding.layoutDetail.setVisibility(View.GONE);
        } else {
            binding.layoutCapure.setVisibility(View.GONE);
            binding.layoutDetail.setVisibility(View.VISIBLE);
        }
    }

    private void initSearchRecyclerView() {
        adapterSearch = new AdaperSearchImag(getBaseContext(), imageSearch);
        binding.recyLoadImage.setAdapter(adapterSearch);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getBaseContext(), 1, RecyclerView.HORIZONTAL, false);
        binding.recyLoadImage.setLayoutManager(layoutManager);
        adapterSearch.setIonClickImage(new IonClickImage() {
            @Override
            public void clickImage(Data image, int i) {
                initViewPager(i);
            }


            @Override
            public void longClickImage(Data image, View view, int i) {
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.inflate(R.menu.menu_search);

                Menu menu = popup.getMenu();
                // com.android.internal.view.menu.MenuBuilder
//                Log.i(LOG_TAG, "Menu class: " + menu.getClass().getName());

                // Register Menu Item Click event.
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.download:
                                ShowDownloadDialog(image);
                                break;
                            case R.id.view:
                                initViewPager(i);
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

    private void ShowDownloadDialog(Data data) {
        List<Shop> shops = new ArrayList<>();
        shops = dbManager.getAllShop();
        String[] shop_name = new String[shops.size()];
        for(int i=0;i<shops.size();i++){
            shop_name[i] = shops.get(i).getName();
        }
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.custom_dialog_shop);
        final ImageView imageView = dialog.findViewById(R.id.custom_img);
        final TextView textView = dialog.findViewById(R.id.custom_title);

        Glide.with(getBaseContext()).load(GenerPrecenter.URL+data.getImg_url()).error(R.drawable.ic_circle_button).into(imageView);
        textView.setText(data.getTitle());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                android.R.layout.simple_spinner_item,
                shop_name);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        final Spinner spinner = dialog.findViewById(R.id.custom_spinner);
        spinner.setAdapter(adapter);

        // When user select a List-Item.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final Button button = dialog.findViewById(R.id.custom_download);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.create();
        dialog.show();
    }

    private void setUpCamera() {
        binding.camera.setLifecycleOwner(this);
        binding.seekBar1.setProgress(0);
        binding.seekBar1.setMax(30);
        binding.camera.setZoom(0.3f);
        binding.seekBar1.setProgress(10);
    }

    private void capture() {
        binding.camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull @NotNull PictureResult result) {
                super.onPictureTaken(result);
                // A Picture was taken!
                // If planning to show a Bitmap, we will take care of
                // EXIF rotation and background threading for you...
                result.toBitmap(1080, 1920, new BitmapCallback() {
                    @Override
                    public void onBitmapReady(@Nullable @org.jetbrains.annotations.Nullable Bitmap bitmap) {
//                        binding.img.setVisibility(View.VISIBLE);
                        ConVertJson(GenerPrecenter.request);


//                        binding.img.setImageBitmap(bitmap);
                        Log.d("mop", "getBitmap");
                    }
                });
                Log.d("mop", "addCameraListener");
                // If planning to save a file on a background thread,
                // just use toFile. Ensure you have permissions.
//                result.toFile(file, callback);

                // Access the raw data if needed.
//                byte[] data = result.getData();

            }
        });
    }

    private void ConVertJson(String request) {
        imageSearch.clear();
        try {
            JSONObject jsonObject = new JSONObject(request);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject js = jsonArray.getJSONObject(i);
                String basePrice = js.getString("basePrice");
                String date = js.getString("date");
                String img_url = js.getString("img_url");
                String km = js.getString("km");
                String repair = js.getString("repair");
                String title = js.getString("title");
                String totalPrice = js.getString("totalPrice");
                String year = js.getString("year");
                Data data = new Data(basePrice, date, img_url, km, repair, title, totalPrice, year);
                Log.d(TAG, "http://192.168.1.222:5000/" + data.getImg_url());
                imageSearch.add(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, e.getMessage());
        }
        adapterSearch.notifyDataSetChanged();
    }

    private void generateDataList(List<Data> photoList) {
        adapterSearch = new AdaperSearchImag(this, photoList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getBaseContext(), 1, RecyclerView.HORIZONTAL, false);
        binding.recyLoadImage.setLayoutManager(layoutManager);
        binding.recyLoadImage.setAdapter(adapterSearch);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void Save(Bitmap bitmap) {
        File filePath = Environment.getExternalStorageDirectory();
        File dir = new File(filePath.getAbsoluteFile() + "/Demo/");
        dir.mkdir();
        File file = new File(dir, System.currentTimeMillis() + ".jpg");
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        Log.d("mop", "myDir.toString()");
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void SaveImage(Bitmap finalBitmap) {
        Log.d("save", "myDir.toString()");
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();

        String fname = GenerPrecenter.createNameImage() + Image.TYPY_IMAGE;
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            Log.d("save", myDir.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GetImage() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File file = new File(directory, "mario" + ".png");
//        imageView.setImageDrawable(Drawable.createFromPath(file.toString()));
    }

    @Override
    public void onBackPressed() {
        if (isCapture) {
            finish();
        } else {
            isCapture = CAPTURE;
            CheckCapture();
        }
    }

    private void ckick() {
        binding.btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        binding.img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.img.setVisibility(View.GONE);
//            }
//        });
//        binding.capure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("mop", "capure");
//                binding.camera.takePicture();
//            }
//        });
//        binding.camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("mop", "cam click");
//                binding.camera.takePicture();
//            }
//        });
        binding.btnZoom15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.seekBar1.setProgress(20);


            }
        });
        binding.btnZoom20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.seekBar1.setProgress(30);


            }
        });
        binding.btnZoom10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.seekBar1.setProgress(10);
            }
        });
        binding.btnZoom05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.seekBar1.setProgress(00);
            }
        });
        binding.seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.camera.setZoom(progress / 30f);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.camera.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.camera.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.camera.destroy();
    }

}