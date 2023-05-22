package com.onicolian.drawer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "wardrobe.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    public static final String CLOTHES = "clothes"; // название таблицы в бд
    public static final String SET = "clothesSet"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_PRISE = "prise";
    public static final String COLUMN_DESK = "desk";
    public static final String COLUMN_FAVORITE = "isFavorite";
    public static final String COLUMN_SETLIST = "setList";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        db.execSQL("CREATE TABLE clothes (" + COLUMN_ID
//                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                + COLUMN_IMAGE + " BLOB, " + COLUMN_CATEGORY + " TEXT, "
//                + COLUMN_DATE + " TEXT, " + COLUMN_PRISE + " INTEGER, "
//                + COLUMN_DESK + " TEXT, " + COLUMN_FAVORITE + " INTEGER);");

        db.execSQL("CREATE TABLE clothesSet (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SETLIST + " TEXT,"
                + COLUMN_DESK + " TEXT);");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS "+CLOTHES);
        db.execSQL("DROP TABLE IF EXISTS "+SET);

        onCreate(db);
    }
}