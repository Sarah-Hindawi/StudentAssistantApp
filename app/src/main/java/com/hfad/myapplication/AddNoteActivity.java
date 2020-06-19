package com.hfad.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddNoteActivity extends AppCompatActivity {

    private String title, note;
    private Cursor c;
    private SQLiteDatabase db;
    private boolean editing;
    private int id;
    private EditText titleET, noteET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MainActivity.theme == 0) getTheme().applyStyle(R.style.AppThemeDark, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        editing = false;
        titleET = (EditText) findViewById(R.id.et);
        noteET = (EditText) findViewById(R.id.et2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);

        SQLiteOpenHelper sqLiteOpenHelper = new SQL(this);
        db = sqLiteOpenHelper.getReadableDatabase();

        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                editing = true;
                id = bundle.getInt("id");
                c = db.query("NOTES", new String[]{"TITLE", "CONTENTS"}, "_id=" + id, null, null, null, null);
                c.moveToFirst();
                titleET.setText(c.getString(0));
                noteET.setText(c.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickDone(View view) {

        title = titleET.getText().toString();
        note = noteET.getText().toString();

        SQLiteOpenHelper oh = new SQL(this);
        db = oh.getReadableDatabase();
        c = db.query("NOTES", new String[]{"_id", "TITLE"}, null, null, null, null, null);

        if (editing) update();
        else insertdb(db, title, note);

        Intent intent = new Intent(AddNoteActivity.this, NotesActivity.class);
        startActivity(intent);
    }

    private static void insertdb(SQLiteDatabase db, String n, String des) {
        ContentValues cv = new ContentValues();
        cv.put("TITLE", n);
        cv.put("CONTENTS", des);
        db.insert("NOTES", null, cv);
    }

    public void update() {
        SQLiteOpenHelper oh = new SQL(this);
        db = oh.getReadableDatabase();
        c = db.query("NOTES", new String[]{"_id", "TITLE"}, null, null, null, null, null);

        ContentValues cv = new ContentValues();
        cv.put("TITLE", title);
        cv.put("CONTENTS", note);
        db.update("NOTES", cv, "_id = " + id, null);
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            c.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}