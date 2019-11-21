package com.example.android.SimpleCalc;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class StandUp extends AppCompatActivity {
    private static final String TAG = "<< StandUp >>";
    private ToggleButton alarmToggle;
    private NotificationManager mNotificationManager;

    private static final int NOTIFICATION_ID = 0;
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final String PRIMARY_CHANNEL_NAME = "Stand up notification";
    private AlarmManager alarmManager;
    private PendingIntent notifyPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stand_up);

        alarmToggle = findViewById(R.id.alarmToggle);

        alarmToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String toastMessage;

                if (isChecked) {
//                    AlarmReceiver.deliverNotification(StandUp.this);
                    toastMessage = "Stand Up Alarm On!";

                    Intent notifyIntent = new Intent(StandUp.this, AlarmReceiver.class);

                    boolean alarmUp = (PendingIntent.getBroadcast(StandUp.this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);
                    alarmToggle.setChecked(alarmUp);

                    notifyPendingIntent = PendingIntent.getBroadcast(StandUp.this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

//                    long repeatInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                    long repeatInterval = 9000;
                    long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;

                    //If the Toggle is turned on, set the repeating alarm with a 15 minute interval
                    if (alarmManager != null) {
                        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, repeatInterval, notifyPendingIntent);
                    }
                } else {
                    toastMessage = "Stand Up Alarm Off!";
                    alarmManager.cancel(notifyPendingIntent);
                    // Keep the call to cancelAll() on the NotificationManager, because turning the alarm toggle off should still remove any existing notification.
                    mNotificationManager.cancelAll();
                }

                Toast.makeText(StandUp.this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        });

        createNotificationChannel();


    }

    private void createNotificationChannel() {
        //Notification manager to handle our notification
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, PRIMARY_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notifies every 15 minutes to stand up and walk");
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableLights(true);

            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }


}
