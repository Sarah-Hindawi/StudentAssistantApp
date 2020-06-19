package com.hfad.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class NotesActivity extends AppCompatActivity {

    private Cursor cursor;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(MainActivity.theme==0)getTheme().applyStyle(R.style.AppThemeDark, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SQLiteOpenHelper oh = new SQL(this);
        sqLiteDatabase = oh.getReadableDatabase();
        cursor = sqLiteDatabase.query("NOTES", new String[]{"_id", "TITLE"}, null, null, null, null, null);

        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                sqLiteDatabase.delete("NOTES", "_id=" + bundle.getInt("id"), null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_list_item_1, cursor, new String[]{"TITLE"}, new int[]{android.R.id.text1});

        ListView listView = (ListView) findViewById(R.id.lv);
        listView.setAdapter(listAdapter);

        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(NotesActivity.this, NoteContentsActivity.class);
                intent.putExtra("chosen", (int) l);
                startActivity(intent);
            }
        };
        listView.setOnItemClickListener(clickListener);
    }

    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.mainmenu, m);
        return super.onCreateOptionsMenu(m);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(mi);
    }

    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        sqLiteDatabase.close();
    }
}