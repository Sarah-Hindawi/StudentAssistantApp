package com.hfad.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssignmentsAdayActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String CHOSEN = "chosen";
    private int assignmentId;
    private String task = "", day;
    private Cursor cursor;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MainActivity.theme == 0) getTheme().applyStyle(R.style.AppThemeDark, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignmens_aday);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);

        assignmentId = (Integer) getIntent().getExtras().get(CHOSEN);
        SQLiteOpenHelper oh = new SQL(this);
        sqLiteDatabase = oh.getReadableDatabase();
        cursor = sqLiteDatabase.query("SCHEDULE",
                new String[]{"DAY", "CONTENTS"}, "_id=?", new String[]{Integer.toString(assignmentId)},
                null, null, null);
        cursor.moveToFirst();
        day = cursor.getString(0);
        task = cursor.getString(1);

        TextView title = (TextView) findViewById(R.id.tv1);
        title.setText(day);
        show();
    }

    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.todolistmenu, m);
        return super.onCreateOptionsMenu(m);
    }

    //reference for pop-up: https://alvinalexander.com/source-code/android-mockup-prototype-dialog-text-field
    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.add:
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Create new task")
                        .setMessage("Enter task name")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = taskEditText.getText() + "0";
                                createTask(name);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(mi);
        }
    }

    public void createTask(String name) {
        ContentValues cv = new ContentValues();
        if (task.length() != 0)
            name = task + ",--," + name;
        cv.put("CONTENTS", name);
        sqLiteDatabase.update("SCHEDULE", cv, "DAY = ?", new String[]{day});
        cursor.close();
        sqLiteDatabase.close();
        show();
    }

    public void show() {
        assignmentId = (Integer) getIntent().getExtras().get(CHOSEN);
        SQLiteOpenHelper oh = new SQL(this);
        sqLiteDatabase = oh.getReadableDatabase();
        cursor = sqLiteDatabase.query("SCHEDULE", new String[]{"DAY", "CONTENTS"}, "_id=?", new String[]{Integer.toString(assignmentId)}, null, null, null);

        cursor.moveToFirst();
        task = cursor.getString(1);

        if (task.length() != 0) {

            cursor = sqLiteDatabase.query("SCHEDULE", new String[]{"DAY", "CONTENTS"}, "_id=?", new String[]{Integer.toString(assignmentId)}, null, null, null);
            cursor.moveToFirst();
            task = cursor.getString(1);
            List<String> tasks = new ArrayList<>(Arrays.asList(task.split(",--,")));

            List<String> tasks2 = new ArrayList<>();
            for (int i = 0; i < tasks.size(); i++) {
                String str = tasks.get(i);
                tasks2.add(str.substring(0, str.length() - 1));
            }

            ViewGroup layout = (ViewGroup) findViewById(R.id.ll);
            layout.removeAllViews();

            for (int i = 0; i < tasks2.size(); i++) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                checkBox.setText(tasks2.get(i));
                checkBox.setTextSize(17);
                checkBox.setTag(i);
                checkBox.setOnClickListener(AssignmentsAdayActivity.this);
                layout.addView(checkBox);
                if (tasks.get(i).charAt(tasks.get(i).length() - 1) == '1')
                    checkBox.setChecked(true);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        sqLiteDatabase.close();
    }

    @Override
    public void onClick(View view) {
        CheckBox checkBox = (CheckBox) view;

        int value = ((CheckBox) view).isChecked() ? 1 : 0;

        assignmentId = (Integer) getIntent().getExtras().get(CHOSEN);
        SQLiteOpenHelper oh = new SQL(this);
        sqLiteDatabase = oh.getReadableDatabase();
        cursor = sqLiteDatabase.query("SCHEDULE", new String[]{"DAY", "CONTENTS"}, "_id=?", new String[]{Integer.toString(assignmentId)}, null, null, null);

        cursor.moveToFirst();
        task = cursor.getString(1);

        List<String> tasks = new ArrayList<>(Arrays.asList(task.split(",--,")));

        int index = (int) checkBox.getTag();
        Log.d("index", Integer.toString(index));
        String modify = tasks.get(index);
        Log.d("index modify", modify);
        tasks.set(index, modify.substring(0, modify.length() - 1) + value);
        task = String.join(",--,", tasks);

        ContentValues cv = new ContentValues();
        cv.put("CONTENTS", task);
        sqLiteDatabase.update("SCHEDULE", cv, "DAY = ?", new String[]{day});
    }
}