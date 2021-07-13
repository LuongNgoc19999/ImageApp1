package com.example.imageapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.imageapp.SQLite.DBManager;
import com.example.imageapp.adapter.AdaperSearchImag;
import com.example.imageapp.adapter.AdapterLocalImage;
import com.example.imageapp.adapter.AdapterPager;
import com.example.imageapp.databinding.ActivityMainBinding;
import com.example.imageapp.fragment.DetailFragment;
import com.example.imageapp.model.Data;
import com.example.imageapp.model.GenerPrecenter;
import com.example.imageapp.model.Image;
import com.example.imageapp.model.IonClickImage;
import com.example.imageapp.model.ItemTouchHelperListener;
import com.example.imageapp.model.RecyclerViewItemTouchHelper;
import com.example.imageapp.model.SharedPrefs;
import com.example.imageapp.model.Shop;
import com.example.imageapp.view.FileActivity;
import com.otaliastudios.cameraview.BitmapCallback;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.PictureResult;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ItemTouchHelperListener {

    private int heightScreen = 0;
    private int widthScreen = 0;
    private int heightCamera = 0;
    private int widthCamera = 0;

    public static final boolean LOCAL = true;
    public static final boolean ONLINE = false;
    private boolean state_detail = LOCAL;
    public static final String KEY_SHARED_PRES_SHOP = "KEY_SHOP";
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
    List<Data> imageLocal = new ArrayList<>();
    AdaperSearchImag adapterSearch;
    AdapterLocalImage adapterLocal;
    FileOutputStream outputStream;
    List<DetailFragment> fragmentList = new ArrayList<>();
    AdapterPager adapterPager;
    DetailFragment detailFragment;
    DBManager dbManager;
    Shop shop = new Shop();
    int viewPagerPosition = 0;
    DisplayMetrics metrics = new DisplayMetrics();

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View v = findViewById(R.id.camera);
        widthCamera = v.getWidth();
        heightCamera = v.getHeight();
        heightScreen = metrics.heightPixels;
        widthScreen = metrics.widthPixels;
    }


    public void initViewPager(List<Data> dataList, int i, boolean isLocal) {
        adapterPager = new AdapterPager(getSupportFragmentManager());
        for (Data data : dataList) {
            detailFragment = new DetailFragment(data, shop.getName(), isLocal, i, MainActivity.this);
            adapterPager.AddFragment(detailFragment);
        }

        binding.viewPager.setAdapter(adapterPager);
        binding.viewPager.setCurrentItem(i);
        binding.indicator.setViewPager(binding.viewPager);
// optional
        adapterPager.registerDataSetObserver(binding.indicator.getDataSetObserver());
        viewPagerPosition = i;
        isCapture = DETAIL;
        CheckCapture();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        dbManager = new DBManager(getBaseContext());
        createDefaulFile();
        setUpCamera();
        ckick();
        CheckCapture();
        capture();
        initSearchRecyclerView();
        initLocalImageRecyclerview();

    }

    private void createDefaulFile() {
//        Log.d("mop", "size1: "+String.valueOf(dbManager.getAllShop().size()));
        if (!dbManager.ifExistsShop()) {
            shop = new Shop(0, "Deaful", new ArrayList<>());
            dbManager.insertShop(shop);
            SharedPrefs.getInstance().put(KEY_SHARED_PRES_SHOP, shop.getName());
//            Log.d("mop", "size2: "+String.valueOf(dbManager.getAllShop().size()));
        } else {
            shop = new Shop(0, SharedPrefs.getInstance().get(KEY_SHARED_PRES_SHOP, String.class), new ArrayList<>());
            Log.d("mop", shop.getName());

        }
    }

//    private void insertShop() {
//        List<Shop> shops = new ArrayList<>();
//        shops.add(new Shop(1,"Shop 1"));
//        shops.add(new Shop(2,"Shop 2"));
//        shops.add(new Shop(3,"Shop 3"));
//        shops.add(new Shop(4,"Shop 4"));
//        for (Shop s: shops){
//            dbManager.insertShop(s);
//        }
//        Log.d("mop", "size: "+String.valueOf(dbManager.getAllShop().size()));
//    }

    private void CheckCapture() {
        if (isCapture) {
            binding.layoutCapure.setVisibility(View.VISIBLE);
            binding.layoutDetail.setVisibility(View.GONE);
        } else {
            binding.layoutCapure.setVisibility(View.GONE);
            binding.layoutDetail.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof AdapterLocalImage.ViewHolder){
            String name = imageLocal.get(viewHolder.getAdapterPosition()).getTitle();
            int id = imageLocal.get(viewHolder.getAdapterPosition()).getId();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.dialog_delete_question_file);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbManager.deleteImageByID(id);
                    updateLocalImageRecyclerView();
                    dialog.cancel();
                }
            });
            Dialog dialog = builder.create();
            dialog.show();
        }
    }

    private void initLocalImageRecyclerview() {
        imageLocal.addAll(dbManager.getAllImage(shop.getName()));
        adapterLocal = new AdapterLocalImage(getBaseContext(), imageLocal);
        binding.recyDownLoadImage.setAdapter(adapterLocal);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getBaseContext(), 1, RecyclerView.VERTICAL, false);
        binding.recyDownLoadImage.setLayoutManager(layoutManager);

        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL);
        binding.recyDownLoadImage.addItemDecoration(decoration);//theem dong ker ben duoi

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.recyDownLoadImage);

        adapterLocal.setIonClickImage(new IonClickImage() {
            @Override
            public void clickImage(Data data, int i) {
                state_detail = LOCAL;
                initViewPager(imageLocal, i, state_detail);
                binding.btnSave.setVisibility(View.GONE);
                binding.btnDelete.setVisibility(View.VISIBLE);
            }

            @Override
            public void longClickImage(Data data, View view, int i) {
//                state_detail = LOCAL;
//                initViewPager(imageLocal, i, state_detail);
            }

            @Override
            public void deleteImage(Data data, int i) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.dialog_delete_question_file);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbManager.deleteImageByID(data.getId());
                        updateLocalImageRecyclerView();
                        dialog.cancel();
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void initSearchRecyclerView() {
        adapterSearch = new AdaperSearchImag(getBaseContext(), imageSearch);
        binding.recyLoadImage.setAdapter(adapterSearch);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getBaseContext(), 1, RecyclerView.HORIZONTAL, false);
        binding.recyLoadImage.setLayoutManager(layoutManager);
        adapterSearch.setIonClickImage(new IonClickImage() {
            @Override
            public void deleteImage(Data data, int i) {
//

            }

            @Override
            public void clickImage(Data image, int i) {
                state_detail = ONLINE;
                initViewPager(imageSearch, i, state_detail);
                binding.btnSave.setVisibility(View.VISIBLE);
                binding.btnDelete.setVisibility(View.GONE);
            }


            @Override
            public void longClickImage(Data image, View view, int i) {
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                popup.inflate(R.menu.menu_search);

                Menu menu = popup.getMenu();
                // com.android.internal.view.menu.MenuBuilder
//                Log.i(LOG_TAG, "Menu class: " + menu.getClass().getName());
                // Register Menu Item Click event
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.download:
//                                ShowDownloadDialog(image);
                                downloadImage(image);
                                break;
                            case R.id.view:
                                state_detail = ONLINE;
                                initViewPager(imageSearch, i, state_detail);
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

    private void downloadImage(Data image) {
        imageLocal.clear();
        dbManager.insertImage(image, shop.getName());
        imageLocal.addAll(dbManager.getAllImage(shop.getName()));
        Log.d("mop", "down:" + shop.getName());
        adapterLocal.notifyDataSetChanged();
    }

    private void ShowDownloadDialog(Data data) {
        List<Shop> shops = new ArrayList<>();
        shops = dbManager.getAllShop();
        String[] shop_name = new String[shops.size()];
        for (int i = 0; i < shops.size(); i++) {
            shop_name[i] = shops.get(i).getName();
        }
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.custom_dialog_shop);
        final ImageView imageView = dialog.findViewById(R.id.custom_img);
        final TextView textView = dialog.findViewById(R.id.custom_title);

        Glide.with(getBaseContext()).load(GenerPrecenter.URL + data.getImg_url()).error(R.drawable.ic_circle_button).into(imageView);
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
                result.toBitmap(1080, heightScreen, new BitmapCallback() {
                    @Override
                    public void onBitmapReady(@Nullable @org.jetbrains.annotations.Nullable Bitmap bitmap) {
                        bitmap = GenerPrecenter.CropImage(bitmap, widthScreen, heightScreen, widthCamera, heightCamera);
                        binding.img.setVisibility(View.VISIBLE);
                        binding.img.setImageBitmap(bitmap);
                        imageSearch.clear();
                        imageSearch.addAll(GenerPrecenter.ConVertJson(GenerPrecenter.request));
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
        binding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
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
            updateLocalImageRecyclerView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==1999)
//        {
//            String name = data.getStringExtra("name");
//            imageLocal.clear();
//            imageLocal.addAll(dbManager.getAllImage(name));
//            adapterLocal.notifyDataSetChanged();
//            SharedPrefs.getInstance().put(KEY_SHARED_PRES_SHOP, name);
//            shop.setName(name);
//        }

    }

    private void checkCounViewPager() {
        List<Data> ds = new ArrayList<>();
        ds.addAll(dbManager.getAllImage(shop.getName()));
        if (ds.size() == 0) {
            isCapture = CAPTURE;

            CheckCapture();
        } else {
            if (ds.size() >= viewPagerPosition) {
                initViewPager(ds, viewPagerPosition, true);
            } else {
                initViewPager(ds, ds.size(), true);
            }
        }
    }

    private void ckick() {
//        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewI
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.dialog_delete_question_file);
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        fragmentList.addAll((Collection<? extends DetailFragment>) adapterPager.getFragments());
                        if (adapterPager.getCount() <= 1) {
                            detailFragment = (DetailFragment) adapterPager.getFragments().get(0);
                            Data data = detailFragment.getData();
                            dbManager.deleteImageByID(data.getId());
                            isCapture = CAPTURE;
                            CheckCapture();
                            updateLocalImageRecyclerView();
                        } else {
                            viewPagerPosition = binding.viewPager.getCurrentItem();
                            detailFragment = (DetailFragment) adapterPager.getFragments().get(viewPagerPosition);
                            Data data = detailFragment.getData();
                            adapterPager.removeFragment(viewPagerPosition);
                            binding.viewPager.setAdapter(adapterPager);
                            dbManager.deleteImageByID(data.getId());
                        }
                        dialog.cancel();
                    }
                });
                Dialog dialog = builder.create();
                dialog.show();
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailFragment = (DetailFragment) adapterPager.getFragments().get(viewPagerPosition);
                Data data = detailFragment.getData();
                Log.d("mop", data.toString());
                Log.d("mop", binding.viewPager.getCurrentItem() + " ");
//                Toast.makeText(getBaseContext(), binding.viewPager.getCurrentItem() + " ", Toast.LENGTH_SHORT).show();
                downloadImage(data);
            }
        });
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPagerPosition = position;
//                Toast.makeText(getBaseContext(), String.valueOf(viewPagerPosition), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.viewPager.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {
            @Override
            public void onAdapterChanged(@NonNull @NotNull ViewPager viewPager, @Nullable @org.jetbrains.annotations.Nullable PagerAdapter oldAdapter, @Nullable @org.jetbrains.annotations.Nullable PagerAdapter newAdapter) {
//                viewPager.setAdapter(newAdapter);
//                if(oldAdapter.getCount()!=newAdapter.getCount()){
//                    viewPager.setAdapter(newAdapter);
//                }
            }
        });

        binding.btnAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.btnViewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), FileActivity.class);
//                intent.putExtra("name", shop.getName());
                startActivity(intent);
//                startActivityForResult(intent, 1999);
            }
        });
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
        updateLocalImageRecyclerView();
    }

    private void updateLocalImageRecyclerView() {
        imageLocal.clear();
        imageLocal.addAll(dbManager.getAllImage(shop.getName()));
        adapterLocal.notifyDataSetChanged();
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