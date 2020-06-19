package com.hfad.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AssignmentsActivity extends AppCompatActivity {

    private Cursor cursor;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MainActivity.theme == 0) getTheme().applyStyle(R.style.AppThemeDark, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SQLiteOpenHelper oh = new SQL(this);
        sqLiteDatabase = oh.getReadableDatabase();
        cursor = sqLiteDatabase.query("SCHEDULE", new String[]{"_id", "DAY"}, null, null, null, null, null);

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_list_item_1, cursor, new String[]{"DAY"}, new int[]{android.R.id.text1});

        ListView listView = (ListView) findViewById(R.id.lv);
        listView.setAdapter(listAdapter);

        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AssignmentsActivity.this, AssignmentsAdayActivity.class);
                intent.putExtra("chosen", (int) l);
                startActivity(intent);
            }
        };
        listView.setOnItemClickListener(clickListener);
    }
}