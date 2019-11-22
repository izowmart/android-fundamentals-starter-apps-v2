package com.example.android.SimpleCalc.notificationTopic;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.android.SimpleCalc.R;


public class NotificationHelper {
    private static final String TAG="<< NotificationHelper>>";



    public static NotificationCompat.Builder getNotificationBuilder(Context context, String title, String body) {

        Log.d(TAG, "getNotificationBuilder: ");
        Intent notificationIntent = new Intent(context, ProfileActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, Notification.NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(context, Notification.NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_notification_24dp)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);




    }
}
