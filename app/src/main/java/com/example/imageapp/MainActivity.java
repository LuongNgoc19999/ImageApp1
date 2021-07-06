package com.example.imageapp;

import android.Manifest;
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
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imageapp.databinding.ActivityMainBinding;
import com.example.imageapp.model.Data;
import com.example.imageapp.model.GenerPrecenter;
import com.example.imageapp.model.Image;
import com.example.imageapp.model.IonClickImage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.PictureResult;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Main";
    public static final float zoom05 = 0.5f;
    public static final float zoom10 = 1f;
    public static final float zoom15 = 1.5f;
    public static final float zoom20 = 2.0f;
    private float zoomState = zoom10;
    ActivityMainBinding binding;
    List<Data> imageSearch = new ArrayList<>();
    AdaperSearchImag adapterSearch;
    Bitmap bitmap;
    FileOutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setUpCamera();
        ckick();
        capture();
        initSearchRecyclerView();
        binding.img.setVisibility(View.VISIBLE);
        Picasso.get()
                .load("http://192.168.1.222:5000/images/VU5596112459.jpg")
                .resize(50, 50)
                .centerCrop()
                .into(binding.img);

    }

    private void initSearchRecyclerView() {
        adapterSearch = new AdaperSearchImag(getBaseContext(), imageSearch);
        binding.recyLoadImage.setAdapter(adapterSearch);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getBaseContext(), 1, RecyclerView.HORIZONTAL, false);
        binding.recyLoadImage.setLayoutManager(layoutManager);
        adapterSearch.setIonClickImage(new IonClickImage() {
            @Override
            public void clickImage(Data image) {

            }

            @Override
            public void longClickImage(Data image, View view) {
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
//                                if(isStoragePermissionGranted()){
//                                    SaveImage(image.getImage());
//                                }
                                Toast.makeText(getBaseContext(), "Download", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.view:
                                Toast.makeText(getBaseContext(), "View", Toast.LENGTH_SHORT).show();
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
                        binding.img.setVisibility(View.VISIBLE);

                        try {
                            JSONObject jsonObject = new JSONObject(GenerPrecenter.request);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            ArrayList<Data> listdata = new ArrayList<>();
                            Gson gson = new Gson();
                            ArrayList joList = gson.fromJson(String.valueOf(jsonArray), ArrayList.class);
//                            ArrayList<String> arr = new ArrayList<>();
//                            arr.addAll(Arrays.asList(joList));
                            for (int i=0;i<jsonArray.length();i++){
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
                                Log.d(TAG, "http://192.168.1.222:5000/"+data.getImg_url());
                                imageSearch.add(data);
                            }
                            adapterSearch = new AdaperSearchImag(getBaseContext(), imageSearch);
                            binding.recyLoadImage.setAdapter(adapterSearch);
//                            adapterSearch.notifyDataSetChanged();




//                            Data d = gson.fromJson(String.valueOf(joList.get(0)), Data.class);
//                            Log.d(TAG, d.toString());
//                            imageSearch.addAll(joList);
//                            adapterSearch.notifyDataSetChanged();
//                            for (int i = 0; i < arr.size(); i++) {
////                                Data d = gson.fromJson(String.valueOf(arr.get(i)), Data.class);
//                                Log.d(TAG, arr.get(i));
//                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, e.getMessage());
                        }


//                        imageSearch.add(new Image("0", "Image0"+Image.TYPY_IMAGE, "", bitmap));
                        adapterSearch.notifyDataSetChanged();

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
//        switch (requestCode) {
//            case REQUEST_CAMERA: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//
//            case 2: {
//
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
//                    SaveImage(bitmap);
//                } else {
//                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
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


    private void ckick() {
        binding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.img.setVisibility(View.GONE);
            }
        });
        binding.capure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mop", "capure");
                binding.camera.takePicture();
            }
        });
        binding.camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("mop", "cam click");
                binding.camera.takePicture();
            }
        });
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