package com.hfad.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class StopAlarmActivity extends AppCompatActivity {

    private Handler hand;
    private int correct, sum;
    private TextView results, question;
    private boolean submitted = true, last = false;
    private String k1 = "correct";
    private String k3 = "sum";
    private String k4 = "submitted";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (MainActivity.theme == 0) getTheme().applyStyle(R.style.AppThemeDark, true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            correct = savedInstanceState.getInt(k1);
            sum = savedInstanceState.getInt(k3);
            submitted = savedInstanceState.getBoolean(k4);
        }
        running();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(k1, correct);
        savedInstanceState.putInt(k3, sum);
        savedInstanceState.putBoolean(k4, submitted);
    }

    public void onClickSubmit(View view) {
        boolean finished = false;
        hand.removeCallbacksAndMessages(null);
        EditText answer = (EditText) findViewById(R.id.answer);

        try {
            if (Integer.parseInt(answer.getText().toString()) == sum) {
                correct++;
                last = true;
                if (correct == 10) {
                    hand.removeCallbacksAndMessages(null);
                    finished = true;
                    try {
                        Intent myService = new Intent(StopAlarmActivity.this, TimerService.class);
                        stopService(myService);
                        if (TimerService.ringtone.isPlaying())
                            TimerService.ringtone.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
            } else
                correct = 0;
            last = true;

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        submitted = false;

        if (!finished) running();
        answer.setText("");
    }

    public void running() {
        question = (TextView) findViewById(R.id.question);
        results = (TextView) findViewById(R.id.results);

        if (!last) correct = 0;
        last = false;
        submitted = true;
        results.setText(getString(R.string.tv1, correct));

        Random r = new Random();
        int n1 = r.nextInt(30);
        int n2 = r.nextInt(30);

        sum = n1 + n2;
        question.setText(getString(R.string.tv2, n1, n2));

        hand = new Handler();
        hand.postDelayed(new Runnable() {

            @Override

            public void run() {
                running();
            }

        }, 8000);
    }
}