package com.example.android.SimpleCalc;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import static android.graphics.Color.RED;

public class Notification extends AppCompatActivity {
    private static final String TAG = "Notification";

    public static final String NOTIFICATION_CHANNEL_ID = "am the notification channel id";
    public static final String NOTIFICATION_CHANNEL_NAME = "SimpleCalc";
    private static final int NOTIFICATION_ID = 0;

    private NotificationManager mNotifyManager;
    private Button button_notify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        button_notify = findViewById(R.id.notify);
        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        Log.d(TAG, "createNotificationChannel: ");
        // NotificationManager class is used by the android system to send a notification
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notification channel is only available in android version 8 and above
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,NOTIFICATION_CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLightColor(RED);
            notificationChannel.enableLights(true);
            notificationChannel.setDescription("SimpleCal notification");
            notificationChannel.enableVibration(true);

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }
    private void sendNotification() {
        Log.d(TAG, "sendNotification: ");
        NotificationCompat.Builder notificationBuilder = getNotificationBuilder();
        mNotifyManager.notify(NOTIFICATION_ID,notificationBuilder.build());

    }

    private NotificationCompat.Builder getNotificationBuilder(){
        Log.d(TAG, "getNotificationBuilder: ");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,NOTIFICATION_ID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("SimpleCalc")
                .setContentText("Application feedback")
                .setSmallIcon(R.drawable.ic_notification_24dp)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

    }
}

