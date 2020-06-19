package com.hfad.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity {

    private NumberPicker nh, nm, ns;
    private int s;
    public static int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(MainActivity.theme==0)getTheme().applyStyle(R.style.AppThemeDark, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDefaultDisplayHomeAsUpEnabled(true);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nh = (NumberPicker) findViewById(R.id.nph);
        nh.setMaxValue(24);
        nm = (NumberPicker) findViewById(R.id.npm);
        nm.setMaxValue(60);
        ns = (NumberPicker) findViewById(R.id.nps);
        ns.setMaxValue(60);
    }

    public void onClickStart(View v) {

        s = ns.getValue() + nh.getValue() * 3600 + nm.getValue() * 60;

        Intent in = new Intent(this, TimerService.class);
        in.putExtra("time", s);
        startService(in);
        time = s;
    }

    public void onClickStop(View v) {
        try {
            Intent myService = new Intent(TimerActivity.this, TimerService.class);
            stopService(myService);
            if (TimerService.ringtone.isPlaying())
                TimerService.ringtone.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}