package com.example.imageapp.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.imageapp.model.Shop;

import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {

    public static final int VERSON = 1;
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
        super(context, DB_NAME, null, VERSON);
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
                ID_SHOP + " Text)";
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
        sqLiteDatabase.update(SHOP, contentValues, ID_SHOP + " =?",
                new String[]{String.valueOf(item.getId())});
        closeDB();
    }


    public boolean deleteAllShop() {
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(SHOP, null, null);
        closeDB();
        return true;
    }

    public List<Shop> getAllShop() {
        List<Shop> shops = new ArrayList<>();
        Shop shop = new Shop(0,"");
        String select = "SELECT * FROM " + SHOP;
        sqLiteDatabase = getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                shop = new Shop(id, name);
                shops.add(shop);
            } while (cursor.moveToNext());
        }
        closeDB();
        return shops;
    }

    public boolean ifExistsShop(Shop shop) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor;
        String checkQuery = "SELECT * FROM " + SHOP +" WHERE "+ID +" = "+shop.getId();
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
