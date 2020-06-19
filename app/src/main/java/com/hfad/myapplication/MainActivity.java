package com.hfad.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static int theme = 1;
    private static MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);

        if (menuItem != null) {
            if (theme == 1) menuItem.setIcon(R.drawable.sun);
            else menuItem.setIcon(R.drawable.night);

        }

    }

    public void notesOnClick(View view) {
        Intent intent = new Intent(MainActivity.this, NotesActivity.class);
        startActivity(intent);
    }

    public void todoOnClick(View view) {
        Intent intent = new Intent(MainActivity.this, ListingtodoActivity.class);
        startActivity(intent);
    }

    public void timerOnClick(View view) {
        Intent intent = new Intent(MainActivity.this, TimerActivity.class);
        startActivity(intent);
    }

    public void assignmentsOnClick(View view) {
        Intent intent = new Intent(MainActivity.this, AssignmentsActivity.class);
        startActivity(intent);
    }

    public void alarmOnClick(View view) {
        Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
        startActivity(intent);
    }

    public void onGradeCalculatorClick(View view) {
        Intent intent = new Intent(MainActivity.this, GradeActivity.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.mainact, m);
        return super.onCreateOptionsMenu(m);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        menuItem = mi;
        if (theme == 1) {
            mi.setIcon(R.drawable.sun);
            theme = 0;
        } else {
            mi.setIcon(R.drawable.night);
            theme = 1;
        }
        return super.onOptionsItemSelected(mi);

    }
}
