package com.hfad.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ListingtodoActivity extends AppCompatActivity {

    private Cursor cursor;
    private SQLiteDatabase sqLiteDatabase;
    private SQLiteOpenHelper sqLiteOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(MainActivity.theme==0)getTheme().applyStyle(R.style.AppThemeDark, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listingtodo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);

        show();
    }

    public void onClickAdd(View v) {

        final EditText taskEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Create a new list")
                .setMessage("Enter list name")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = String.valueOf(taskEditText.getText());
                        insertDatabase(sqLiteDatabase, name);
                        //TodoList.addList(name);
                        //adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.mainmenu, m);
        return super.onCreateOptionsMenu(m);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        onClickAdd(findViewById(R.id.tb));
        return super.onOptionsItemSelected(mi);
    }

    private void insertDatabase(SQLiteDatabase db, String title) {

        db = sqLiteOpenHelper.getReadableDatabase();
        cursor = db.query("NOTES", new String[]{"_id", "TITLE"}, null, null, null, null, null);

        ContentValues cv = new ContentValues();
        cv.put("TITLE", title);
        cv.put("TODO", "");
        db.insert("TODO", null, cv);
        show();
    }

    public void show() {

        sqLiteOpenHelper = new SQL(this);
        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        cursor = sqLiteDatabase.query("TODO", new String[]{"_id", "TITLE"}, null, null, null, null, null);

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_list_item_1, cursor, new String[]{"TITLE"}, new int[]{android.R.id.text1});

        ListView listView = (ListView) findViewById(R.id.lv);
        listView.setAdapter(listAdapter);

        AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListingtodoActivity.this, ToDoListActivity.class);
                intent.putExtra(TodoList.CHOSEN, (int) l);
                startActivity(intent);
            }
        };
        listView.setOnItemClickListener(clickListener);
    }

    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        sqLiteDatabase.close();
    }
}