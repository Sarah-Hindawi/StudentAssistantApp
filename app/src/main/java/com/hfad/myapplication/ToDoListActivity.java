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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToDoListActivity extends AppCompatActivity implements View.OnClickListener {

    private int numberOfTasks, todoId;
    private ViewGroup viewGroup;
    private String task = "", title;
    private Cursor cursor;
    private SQLiteDatabase sqLiteDatabase;
    private boolean added;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MainActivity.theme == 0) getTheme().applyStyle(R.style.AppThemeDark, true);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        toolbar.setTitle(title);

        todoId = (Integer) getIntent().getExtras().get(TodoList.CHOSEN);

        SQLiteOpenHelper oh = new SQL(this);
        sqLiteDatabase = oh.getReadableDatabase();
        cursor = sqLiteDatabase.query("TODO", new String[]{"TITLE", "TODO"}, "_id=?",
                new String[]{Integer.toString(todoId)}, null, null, null);
        cursor.moveToFirst();

        title = cursor.getString(0);
        toolbar.setTitle(title);
        task = cursor.getString(1);
        viewGroup = (ViewGroup) findViewById(R.id.checkBoxes);
        numberOfTasks = viewGroup.getChildCount();

        TextView tv = (TextView) findViewById(R.id.titleTodo);
        tv.setText(title);

        show();
        updateProgress();
    }

    /**
     * show the stored elements in the arraylist of the chosen row
     */
    public void show() {
        todoId = (Integer) getIntent().getExtras().get("chosen");
        SQLiteOpenHelper oh = new SQL(this);
        sqLiteDatabase = oh.getReadableDatabase();
        cursor = sqLiteDatabase.query("TODO", new String[]{"TITLE", "TODO"}, "_id=?", new String[]{Integer.toString(todoId)}, null, null, null);

        cursor.moveToFirst();
        task = cursor.getString(1);

        if (task.length() != 0) {

            cursor = sqLiteDatabase.query("TODO", new String[]{"TITLE", "TODO"}, "_id=?", new String[]{Integer.toString(todoId)}, null, null, null);
            cursor.moveToFirst();
            task = cursor.getString(1);
            List<String> tasks = new ArrayList<>(Arrays.asList(task.split(",--,")));

            Log.d("here", task);
            List<String> tasks2 = new ArrayList<>();
            for (int i = 0; i < tasks.size(); i++) {
                String str = tasks.get(i);
                tasks2.add(str.substring(0, str.length() - 1));
            }

            ViewGroup layout = (ViewGroup) findViewById(R.id.checkBoxes);
            layout.removeAllViews();

            for (int i = 0; i < tasks2.size(); i++) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                checkBox.setText(tasks2.get(i));
                checkBox.setTextSize(17);
                checkBox.setOnClickListener(ToDoListActivity.this);
                checkBox.setTag(i);
                layout.addView(checkBox);
                if (tasks.get(i).charAt(tasks.get(i).length() - 1) == '1')
                    checkBox.setChecked(true);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.todolistmenu, m);
        return super.onCreateOptionsMenu(m);
    }


    //reference for pop-up https://alvinalexander.com/source-code/android-mockup-prototype-dialog-text-field
    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        //save checklist to internal storage

        switch (mi.getItemId()) {
            case R.id.add:
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Create a new task")
                        .setMessage("Enter task name")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = String.valueOf(taskEditText.getText() + "0");
                                createTask(name);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            default:
                Intent intent = new Intent(this, ListingtodoActivity.class);
                intent.putExtra("id", todoId);
                startActivity(intent);
                updateProgress();
                return super.onOptionsItemSelected(mi);
        }
    }

    public void createTask(String name) {
        ContentValues cv = new ContentValues();
        if (task.length() != 0)
            name = task + ",--," + name;
        cv.put("TODO", name);
        sqLiteDatabase.update("TODO", cv, "TITLE = ?", new String[]{title});
        cursor.close();
        sqLiteDatabase.close();
        added = true;
        show();
        updateProgress();

    }

    public void onClickCheck(View v) {
        viewGroup = (ViewGroup) findViewById(R.id.checkBoxes);
        numberOfTasks = viewGroup.getChildCount();
        added = true;
        updateProgress();
    }

    public void updateProgress() {
        viewGroup = (ViewGroup) findViewById(R.id.checkBoxes);
        numberOfTasks = viewGroup.getChildCount();
        int numberChecked = 0;
        progressBar = (ProgressBar) findViewById(R.id.pb);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) viewGroup.getChildAt(i);
            if (checkBox.isChecked()) numberChecked++;
        }

        if (numberOfTasks > 1) {
            progressBar.setProgress(numberChecked * 100 / numberOfTasks);
            if (numberChecked * 100 / numberOfTasks == 100) showToast();
        } else if (numberOfTasks == numberChecked) {
            progressBar.setProgress(100);
            showToast();
        } else progressBar.setProgress(0);
    }

    public void showToast() {
        if (viewGroup != null && added && progressBar.getProgress() == 100) {
            CharSequence text = getResources().getString(R.string.complete);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        }
    }

    @Override
    public void onClick(View view) {

        onClickCheck(view);
        CheckBox checkBox = (CheckBox) view;

        int value = ((CheckBox) view).isChecked() ? 1 : 0;

        todoId = (Integer) getIntent().getExtras().get("chosen");
        SQLiteOpenHelper oh = new SQL(this);
        sqLiteDatabase = oh.getReadableDatabase();
        cursor = sqLiteDatabase.query("TODO", new String[]{"TITLE", "TODO"}, "_id=?", new String[]{Integer.toString(todoId)}, null, null, null);

        cursor.moveToFirst();
        task = cursor.getString(1);
        List<String> tasks = new ArrayList<>(Arrays.asList(task.split(",--,")));

        int index = (int) checkBox.getTag();
        String modify = tasks.get(index);
        tasks.set(index, modify.substring(0, modify.length() - 1) + value);
        task = String.join(",--,", tasks);

        ContentValues cv = new ContentValues();
        cv.put("TODO", task);
        sqLiteDatabase.update("TODO", cv, "TITLE = ?", new String[]{title});
    }
}