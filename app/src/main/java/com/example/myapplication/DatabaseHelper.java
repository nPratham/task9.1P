package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "LostFound.db";
    public static final String TABLE_NAME = "advert_data";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "POST_TYPE TEXT," +
                "NAME TEXT," +
                "PHONE TEXT," +
                "DESCRIPTION TEXT," +
                "DATE TEXT," +
                "LOCATION TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String postType, String name, String phone, String desc, String date, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("POST_TYPE", postType);
        cv.put("NAME", name);
        cv.put("PHONE", phone);
        cv.put("DESCRIPTION", desc);
        cv.put("DATE", date);
        cv.put("LOCATION", location);
        return db.insert(TABLE_NAME, null, cv) != -1;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean deleteItem(String itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, "ID = ?", new String[]{itemId});
        return result > 0;
    }
}

