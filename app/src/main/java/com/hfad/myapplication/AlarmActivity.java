package com.hfad.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmActivity extends AppCompatActivity {
    public static int hour, minute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(MainActivity.theme==0)getTheme().applyStyle(R.style.AppThemeDark, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TimePicker timePicker = (TimePicker) findViewById(R.id.tp);
        timePicker.setIs24HourView(true);
    }

    public void onClickStart(View v) {
        TimePicker timePicker = (TimePicker) findViewById(R.id.tp);
        hour = timePicker.getHour();
        minute = timePicker.getMinute();

        EditText msg = (EditText) findViewById(R.id.msg);

        Intent in = new Intent(this, TimerService.class);
        in.putExtra("alarm", msg.getText().toString());
        startService(in);
    }

    public void onClickStop(View v) {
        Intent intent;
        if (TimerService.ringtone.isPlaying()) {
            intent = new Intent(AlarmActivity.this, StopAlarmActivity.class);
            startActivity(intent);
        }
    }
}