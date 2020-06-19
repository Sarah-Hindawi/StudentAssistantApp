package com.hfad.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQL extends SQLiteOpenHelper {

    SQL(Context c) {
        super(c, "sql", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE NOTES ("
                + " _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "TITLE TEXT,"
                + "CONTENTS TEXT);");

        db.execSQL("CREATE TABLE TODO ("
                + " _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "TITLE TEXT,"
                + "TODO TEXT);");

        db.execSQL("CREATE TABLE SCHEDULE ("
                + " _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "DAY TEXT,"
                + "CONTENTS TEXT);");

        insertdb(db, "Monday");
        insertdb(db, "Tuesday");
        insertdb(db, "Wednesday");
        insertdb(db, "Thursday");
        insertdb(db, "Friday");
        insertdb(db, "Saturday");
        insertdb(db, "Sunday");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    private static void insertdb(SQLiteDatabase db, String day) {
        ContentValues cv = new ContentValues();
        cv.put("DAY", day);
        cv.put("CONTENTS", "");
        db.insert("SCHEDULE", null, cv);
    }
}