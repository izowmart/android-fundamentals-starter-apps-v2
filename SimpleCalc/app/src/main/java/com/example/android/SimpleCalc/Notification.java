package com.example.android.SimpleCalc;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import static android.graphics.Color.RED;

public class Notification extends AppCompatActivity {
    private static final String TAG = "Notification";

    public static final String NOTIFICATION_CHANNEL_ID = "am the notification channel id";
    public static final String NOTIFICATION_CHANNEL_NAME = "SimpleCalc";
    private static final int NOTIFICATION_ID = 0;
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.example.android.SimpleCalc.ACTION_UPDATE_NOTIFICATION";

    private NotificationManager mNotifyManager;
    private Button button_notify;
    private Button button_cancel;
    private Button button_update;

    // Initialize the broadcast receiver with its constructor.
    private NotificationReceiver mReceiver = new NotificationReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        // Register the broadcast receiver here
        registerReceiver(mReceiver,new IntentFilter(ACTION_UPDATE_NOTIFICATION));

        button_notify = findViewById(R.id.notify);
        button_notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });
        button_update = findViewById(R.id.update);
        createNotificationChannel();

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Update the notification
                updateNotification();
            }
        });

        button_cancel = findViewById(R.id.cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Cancel the notification
                cancelNotification();
            }
        });
        setNotificationButtonState(true, false, false);
    }

    public void updateNotification() {
        //converting the drawable image into a bitmap.
        setNotificationButtonState(false, false, true);
        Bitmap notificationImg = BitmapFactory.decodeResource(getResources(), R.drawable.mascot_1);

        NotificationCompat.Builder notificationBuilder = getNotificationBuilder();
        notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(notificationImg)
                .setBigContentTitle("Notification update"));

        mNotifyManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    void setNotificationButtonState(Boolean isNotifyEnabled, Boolean isUpdateEnabled, Boolean isCancelEnabled) {
        button_notify.setEnabled(isNotifyEnabled);
        button_cancel.setEnabled(isCancelEnabled);
        button_update.setEnabled(isUpdateEnabled);
    }

    public void cancelNotification() {
        setNotificationButtonState(true, false, false);
        mNotifyManager.cancel(NOTIFICATION_ID);
    }

    private void createNotificationChannel() {
        Log.d(TAG, "createNotificationChannel: ");
        // NotificationManager class is used by the android system to send a notification
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notification channel is only available in android version 8 and above
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setLightColor(RED);
            notificationChannel.enableLights(true);
            notificationChannel.setDescription("SimpleCal notification");
            notificationChannel.enableVibration(true);

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private void sendNotification() {
        Log.d(TAG, "sendNotification: ");
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(this,NOTIFICATION_ID,updateIntent,PendingIntent.FLAG_ONE_SHOT);

        setNotificationButtonState(false, true, true);
        NotificationCompat.Builder notificationBuilder = getNotificationBuilder();
        notificationBuilder.addAction(R.drawable.ic_update,"UPDATE NOTIFICATION",broadcastIntent);

        mNotifyManager.notify(NOTIFICATION_ID, notificationBuilder.build());

    }

    private NotificationCompat.Builder getNotificationBuilder() {
        Log.d(TAG, "getNotificationBuilder: ");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("SimpleCalc")
                .setContentText("Application feedback")
                .setSmallIcon(R.drawable.ic_notification_24dp)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

    }
//    @Override
//    protected void onDestroy() {
//        unregisterReceiver(mReceiver);
//        super.onDestroy();
//    }

    public class NotificationReceiver extends BroadcastReceiver {

        public NotificationReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            updateNotification();
        }
    }

}

