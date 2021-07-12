package com.example.imageapp.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.imageapp.model.Data;
import com.example.imageapp.model.Shop;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {

    public static final int VERSON = 2;
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    Cursor cursor;

    public static final String DB_NAME = "ImageApp";
    public static final String ID = "ID";

    public static final String SHOP = "SHOP";
    public static final String ID_SHOP = "ID_SHOP";
    public static final String NAME_SHOP = "NAME_SHOP";

    public static final String DATA = "DATA";
    public static final String BASE_PRICE = "BASE_PRICE";
    public static final String DATE = "DATE";
    public static final String IMG_URL = "IMG_URL";
    public static final String KM = "KM";
    public static final String REPAIR = "REPAIR";
    public static final String TITTLE = "TITTLE";
    public static final String TOTALPRICE = "TOTALPRICE";
    public static final String YEAR = "YEAR";


    public DBManager(Context context) {
        super(context, DB_NAME, null, VERSON, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreateInfo = "CREATE TABLE " + SHOP + " ( " +
                ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                NAME_SHOP + " Text)";
        //Chạy câu lệnh tạo bảng product
        db.execSQL(queryCreateInfo);
        queryCreateInfo = "CREATE TABLE " + DATA + " ( " +
                ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                BASE_PRICE + " Text, " +
                DATE + " Text, " +
                IMG_URL + " Text, " +
                KM + " Text, " +
                REPAIR + " Text, " +
                TITTLE + " Text, " +
                TOTALPRICE + " Text, " +
                YEAR + " Text, " +
                NAME_SHOP + " Text)";
        //Chạy câu lệnh tạo bảng product
        db.execSQL(queryCreateInfo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        if (i != i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SHOP);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATA);
            onCreate(sqLiteDatabase);
        }
    }

    public void insertImage(Data item, String name_shop) {
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();

        contentValues.put(BASE_PRICE, item.getBasePrice());
        contentValues.put(DATE, item.getDate());
        contentValues.put(IMG_URL, item.getImg_url());
        contentValues.put(KM, item.getKm());
        contentValues.put(REPAIR, item.getRepair());
        contentValues.put(TITTLE, item.getTitle());
        contentValues.put(TOTALPRICE, item.getTotalPrice());
        contentValues.put(YEAR, item.getYear());
        contentValues.put(NAME_SHOP, name_shop);
        sqLiteDatabase.insert(DATA, null, contentValues);
        closeDB();
    }




    public int deleteImageByName(String name) {
        sqLiteDatabase = getWritableDatabase();
        return Long.valueOf(sqLiteDatabase.delete(DATA, TITTLE + " = ?", new String[]{String.valueOf(name)})).intValue();
    }
    public int deleteImageByID(int id) {
        sqLiteDatabase = getWritableDatabase();
        return Long.valueOf(sqLiteDatabase.delete(DATA, ID + " = ?", new String[]{String.valueOf(id)})).intValue();
    }

    public int deleteImageByShop(String name) {
        sqLiteDatabase = getWritableDatabase();
        return Long.valueOf(sqLiteDatabase.delete(DATA, NAME_SHOP + " = ?", new String[]{String.valueOf(name)})).intValue();
    }

    public List<Data> getAllImage(String nameFile) {
        List<Data> resuilt = new ArrayList<>();
        Data data = new Data();
        String select = "SELECT * FROM " + DATA + " WHERE " + NAME_SHOP + "= '" + nameFile + "'";
        sqLiteDatabase = getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String basePrice = cursor.getString(1);
                String date = cursor.getString(2);
                String img_url = cursor.getString(3);
                String km = cursor.getString(4);
                String repair = cursor.getString(5);
                String title = cursor.getString(6);
                String totalPrice = cursor.getString(7);
                String year = cursor.getString(8);
                data = new Data(id, basePrice, date, img_url, km, repair, title, totalPrice, year);
                resuilt.add(data);
            } while (cursor.moveToNext());
        }
        closeDB();
        return resuilt;
    }

    public boolean ifExistsImage(Shop shop) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor;
        String checkQuery = "SELECT * FROM " + DATA + " WHERE " + NAME_SHOP + " = " + shop.getName();
        cursor = database.rawQuery(checkQuery, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
//    public boolean ifExistsImage() {
//        SQLiteDatabase database = getReadableDatabase();
//        Cursor cursor;
//        String checkQuery = "SELECT * FROM " + SHOP;
//        cursor = database.rawQuery(checkQuery, null);
//        boolean exists = (cursor.getCount() > 0);
//        cursor.close();
//        return exists;
//    }

    //------------------------------------------------------------------------------------------------------------


    public void insertShop(Shop item) {
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();

        contentValues.put(NAME_SHOP, item.getName());
        sqLiteDatabase.insert(SHOP, null, contentValues);
        closeDB();
    }

    public void updateShop(Shop item) {
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();

        contentValues.put(NAME_SHOP, item.getName());
        sqLiteDatabase.update(SHOP, contentValues, ID + " =?",
                new String[]{String.valueOf(item.getId())});
        closeDB();
    }


    public boolean deleteAllShop() {
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(SHOP, null, null);
        sqLiteDatabase.delete(DATA, null, null);
        closeDB();
        return true;
    }

    public int deleteShop(String name) {
        sqLiteDatabase = getWritableDatabase();
        return Long.valueOf(sqLiteDatabase.delete(SHOP, NAME_SHOP + " = ?", new String[]{String.valueOf(name)})).intValue();
    }

    public List<Shop> getAllShop() {
        List<Shop> shops = new ArrayList<>();
        Shop shop;
        String select = "SELECT * FROM " + SHOP;
        sqLiteDatabase = getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                shop = new Shop(id, name, new ArrayList<>());
                shops.add(shop);
            } while (cursor.moveToNext());
        }
        closeDB();
        return shops;
    }

    public boolean ifExistsShop(Shop shop) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor;
        String checkQuery = "SELECT * FROM " + SHOP + " WHERE " + ID + " = " + shop.getId();
        cursor = database.rawQuery(checkQuery, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
    public boolean ifExistsShop(String name) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor;
        String checkQuery = "SELECT * FROM " + SHOP + " WHERE " + NAME_SHOP + " = '" + name+"'";
        cursor = database.rawQuery(checkQuery, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean ifExistsShop() {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor;
        String checkQuery = "SELECT * FROM " + SHOP;
        cursor = database.rawQuery(checkQuery, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public int getMaxId() {
        int id = 0;
        String select = "SELECT MAX(" + ID_SHOP + ") FROM " + SHOP;
        sqLiteDatabase = getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        closeDB();
        return id;
    }

    //------------------------------------------------------------------------------------------------------------
    private void closeDB() {
        //dong
        if (sqLiteDatabase != null) sqLiteDatabase.close();
        if (contentValues != null) contentValues.clear();
        if (cursor != null) cursor.close();
    }

}
