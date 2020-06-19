package com.hfad.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimerService extends Service {

    public static Ringtone ringtone;

    public TimerService() {
    }


    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean alarm = true;
        String msg = "Time is up!";
        if (intent.getStringExtra("alarm") != null) {
            msg = intent.getStringExtra("alarm");
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            String current = dateFormat.format(date);
            final int currentHour = Integer.parseInt(current.substring(0, 2));
            final int currentMin = Integer.parseInt(current.substring(3, 5));

            ringtone = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));

            while (true) {
                if (currentHour == AlarmActivity.hour && currentMin == AlarmActivity.minute) break;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            alarm = false;

            try {
                Thread.sleep(TimerActivity.time * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //reference: https://stackoverflow.com/questions/16045722/android-notification-is-not-showing
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "44444")
                .setSmallIcon(android.R.drawable.star_on)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{0, 60000})
                .setAutoCancel(true)
                .setContentTitle("Timer")
                .setContentText(msg);

        if (alarm) {
            builder.setContentTitle("Alarm");
        }

        Intent actionIntent;
        if (alarm) actionIntent = new Intent(this, StopAlarmActivity.class);
        else
            actionIntent = new Intent(this, TimerActivity.class);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(
                this,
                0,
                actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(actionPendingIntent);


        NotificationChannel notificationChannel = new NotificationChannel("44444", "name1", NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(notificationChannel);

        notificationManager.notify(1, builder.build());

        startForeground(1, builder.build());
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        ringtone.play();

        return super.onStartCommand(intent, flags, startId);

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}