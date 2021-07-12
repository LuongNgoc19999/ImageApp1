package com.example.imageapp.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.imageapp.MainActivity;
import com.example.imageapp.R;
import com.example.imageapp.SQLite.DBManager;
import com.example.imageapp.adapter.AdaperSearchImag;
import com.example.imageapp.adapter.AdapterLocalImage;
import com.example.imageapp.adapter.AdapterPager;
import com.example.imageapp.databinding.ActivityGroupCameraBinding;
import com.example.imageapp.fragment.DetailFragment;
import com.example.imageapp.model.Data;
import com.example.imageapp.model.GenerPrecenter;
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

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GroupCameraActivity extends AppCompatActivity {
    int viewPagerPosition = 0;
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
    ActivityGroupCameraBinding binding;
    List<Data> imageSearch = new ArrayList<>();
    List<Data> imageLocal = new ArrayList<>();
    AdaperSearchImag adapterSearch;
    AdapterLocalImage adapterLocal;
    FileOutputStream outputStream;
    List<Fragment> fragmentList = new ArrayList<>();
    AdapterPager adapterPager;
    DetailFragment detailFragment;
    DBManager dbManager;
    Shop shop = new Shop();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_camera);
        getSupportActionBar().hide();
        getintent();
        dbManager = new DBManager(getBaseContext());
        setUpCamera();
        ckick();
        CheckCapture();
        capture();
        initSearchRecyclerView();
        initLocalImageRecyclerview();
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

    private void initLocalImageRecyclerview() {
        imageLocal.addAll(dbManager.getAllImage(shop.getName()));
        adapterLocal = new AdapterLocalImage(getBaseContext(), imageLocal);
        binding.recyDownLoadImage.setAdapter(adapterLocal);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getBaseContext(), 1, RecyclerView.VERTICAL, false);
        binding.recyDownLoadImage.setLayoutManager(layoutManager);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupCameraActivity.this);
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
    private void downloadImage(Data image) {
        imageLocal.clear();
        dbManager.insertImage(image, shop.getName());
        imageLocal.addAll(dbManager.getAllImage(shop.getName()));
        Log.d("mop", "down:" + shop.getName());
        adapterLocal.notifyDataSetChanged();
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
                PopupMenu popup = new PopupMenu(GroupCameraActivity.this, view);
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


    public void getintent() {
        Intent intent = getIntent();
        shop = (Shop) intent.getSerializableExtra("group");
        Log.d("group", shop.toString());
        binding.fileName.setText(shop.getName());
    }

    public void initViewPager(List<Data> dataList, int i, boolean isLocal) {
        adapterPager = new AdapterPager(getSupportFragmentManager());
        for (Data data : dataList) {
            detailFragment = new DetailFragment(data, shop.getName(), isLocal, i, GroupCameraActivity.this);
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
    }

//    private void ConVertJson(String request) {
//        imageSearch.clear();
//        try {
//            JSONObject jsonObject = new JSONObject(request);
//            JSONArray jsonArray = jsonObject.getJSONArray("data");
//            Gson gson = new Gson();
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject js = jsonArray.getJSONObject(i);
//                String basePrice = js.getString("basePrice");
//                String date = js.getString("date");
//                String img_url = js.getString("img_url");
//                String km = js.getString("km");
//                String repair = js.getString("repair");
//                String title = js.getString("title");
//                String totalPrice = js.getString("totalPrice");
//                String year = js.getString("year");
//                Data data = new Data(0, basePrice, date, img_url, km, repair, title, totalPrice, year);
//                Log.d(TAG, "http://192.168.1.222:5000/" + data.getImg_url());
//                imageSearch.add(data);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.d(TAG, e.getMessage());
//        }
//        adapterSearch.notifyDataSetChanged();
//    }

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

    private void ckick() {
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupCameraActivity.this);
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
                        if(adapterPager.getCount()<=1){
                            detailFragment = (DetailFragment) adapterPager.getFragments().get(0);
                            Data data = detailFragment.getData();
                            dbManager.deleteImageByID(data.getId());
                            isCapture = CAPTURE;
                            CheckCapture();
                            updateLocalImageRecyclerView();
                        }else {
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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

