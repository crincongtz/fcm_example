package com.crincongtz.fcmexample;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by crincon on 6/6/18.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.v(TAG, "--------------------");

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            processMessage(remoteMessage);
        }
    }

    private void processMessage(RemoteMessage remoteMessage) {
        Log.v(TAG, "processMessage()");

        Map<String, String> data = remoteMessage.getData();
        Log.d(TAG, "Message data payload: " + data);

        Log.d(TAG, "--------------------");

        String title = "FCM Example";
        String message = "Hello World";
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
        }

        String CHANNEL_ID = getString(R.string.default_notification_channel_id);

        Intent resultIntent;
        resultIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 22, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);
        nBuilder.setSmallIcon(getNotificationIcon(nBuilder))
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.dafault_notification_text);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(123, nBuilder.build());
    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
            return R.drawable.ic_notifications;
        } else {
            return R.drawable.ic_notifications;
        }
    }

}
